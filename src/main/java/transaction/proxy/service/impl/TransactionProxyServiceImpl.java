package transaction.proxy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import transaction.proxy.entity.TransactionProxy;
import transaction.proxy.repository.TransactionProxyRepository;
import transaction.proxy.service.TransactionApiService;
import transaction.proxy.service.TransactionProxyService;
import transaction.proxy.service.mapper.TransactionMapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
@Service
@RequiredArgsConstructor
public class TransactionProxyServiceImpl implements TransactionProxyService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionProxyServiceImpl.class);
    private static final int HEADS_NUMBER = 2;
    private static final int BATCH_SIZE = 2000;

    private final ObjectMapper objectMapper;
    private final TransactionApiService apiService;
    private final TransactionMapper transactionMapper;
    private final TransactionProxyRepository transactionProxyRepository;

    @Override
    public void saveReceivedTransactions(Map<String, String> queryParams) {
        LOGGER.info("Fetching transactions file from external API with query params: {}", queryParams);

        ResponseEntity<byte[]> apiResponse = apiService.getTransactionsFile(queryParams);

        if (!apiResponse.getStatusCode().is2xxSuccessful() || !apiResponse.hasBody()) {
            LOGGER.error("Failed to fetch data from external API. Status: {}, Body present: {}",
                    apiResponse.getStatusCode(), apiResponse.hasBody());
            return;
        }

        LOGGER.info("Successfully fetched transactions file. Starting processing");
        saveTransactionsFromZip(apiResponse.getBody(), Charset.forName("Windows-1251"));
    }

    @Override
    public List<Map<String, Object>> getAndAddHashToLastLoad() {
        LOGGER.info("Fetching last loaded transactions and adding hashes.");
        return addHashToData(apiService.getLastLoad());
    }

    @Override
    public List<Map<String, Object>> getAndAddHashToTransaction(Map<String, String> queryParams) {
        LOGGER.info("Fetching transactions with query params: {} and adding hashes", queryParams);
        return addHashToData(apiService.getListTransactions(queryParams));
    }

    private void saveTransactionsFromZip(byte[] zipData, Charset inputEncoding) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            if (zis.getNextEntry() == null) {
                LOGGER.error("Zip archive is empty");
                throw new RuntimeException("Zip archive is empty");
            }

            LOGGER.info("Processing transactions from zip archive");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(zis, inputEncoding))) {
                List<TransactionProxy> batch = new ArrayList<>();
                String line;

                for (int i = 0; i < HEADS_NUMBER; i++) {
                    reader.readLine();
                }

                while ((line = reader.readLine()) != null) {
                    TransactionProxy transaction = transactionMapper.lineToTransactionProxy(line);
                    batch.add(transaction);

                    if (batch.size() == BATCH_SIZE) {
                        saveBatch(batch);
                    }
                }

                if (!batch.isEmpty()) {
                    saveBatch(batch);
                }
                LOGGER.info("Data saved successfully");
            }
        } catch (IOException e) {
            LOGGER.error("Error processing zip archive: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void saveBatch(List<TransactionProxy> batch) {
        LOGGER.info("Saving batch of {} transactions to the database", batch.size());
        transactionProxyRepository.saveAll(batch);
        batch.clear();
    }

    private List<Map<String, Object>> addHashToData(List<Map<String, Object>> data) {
        LOGGER.info("Adding hashes to {} transactions.", data.size());
        return data.stream()
                .map(transaction -> {
                    try {
                        String transactionDataAsLine = objectMapper.writeValueAsString(transaction);
                        String transactionHash = DigestUtils.sha256Hex(transactionDataAsLine);
                        transaction.put("hash", transactionHash);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error converting transaction to JSON: {}", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                    return transaction;
                }).toList();
    }
}

