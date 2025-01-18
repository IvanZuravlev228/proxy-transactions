package transaction.proxy.service.mapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import transaction.proxy.entity.TransactionProxy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.StringReader;

@Component
public class TransactionMapper {
    private static final Logger LOGGER = LogManager.getLogger(TransactionMapper.class);
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setQuote('"')
            .build();

    public TransactionProxy lineToTransactionProxy(String csvLine) throws IOException {
        LOGGER.debug("Starting to parse CSV line: {}", csvLine);

        try (CSVParser csvParser = new CSVParser(new StringReader(csvLine), CSV_FORMAT)) {
            CSVRecord record = csvParser.iterator().next();

            TransactionProxy proxy = new TransactionProxy();
            proxy.setId(Long.parseLong(record.get(0)));
            proxy.setDocVob(record.get(1));
            proxy.setDocVobName(record.get(2));
            proxy.setDocNumber(record.get(3));
            proxy.setDocDate(record.get(4));
            proxy.setDocVDate(record.get(5));
            proxy.setTransDate(record.get(6));
            proxy.setAmount(record.get(7));
            proxy.setAmountCop(Long.parseLong(record.get(8)));
            proxy.setCurrency(record.get(9));
            proxy.setPayerEdrpou(record.get(10));
            proxy.setPayerName(record.get(11));
            proxy.setPayerAccount(record.get(12));
            proxy.setPayerMfo(record.get(13));
            proxy.setPayerBank(record.get(14));
            proxy.setPayerEdrpouFact(record.get(15));
            proxy.setPayerNameFact(record.get(16));
            proxy.setReciptEdrpou(record.get(17));
            proxy.setReciptName(record.get(18));
            proxy.setReciptAccount(record.get(19));
            proxy.setReciptMfo(record.get(20));
            proxy.setReciptBank(record.get(21));
            proxy.setReciptEdrpouFact(record.get(22));
            proxy.setReciptNameFact(record.get(23));
            proxy.setPaymentDetails(record.get(24));
            proxy.setDocAddAttr(record.get(25));
            proxy.setRegionId(record.get(26));
            proxy.setPaymentType(record.get(27));
            proxy.setPaymentData(record.get(28));
            proxy.setSourceId(Integer.valueOf(record.get(29)));
            proxy.setSourceName(record.get(30));
            proxy.setKekv(record.get(31));
            proxy.setKpk(record.get(32));
            proxy.setContractId(record.get(33));
            proxy.setContractNumber(record.get(34));
            proxy.setBudgetCode(record.get(35));
            proxy.setSystemKey(record.get(36));
            proxy.setSystemKeyFf(record.get(37));
            proxy.setHash(DigestUtils.sha256Hex(csvLine));
            LOGGER.debug("Successfully parsed CSV line into TransactionProxy: {}", proxy);
            return proxy;
        } catch (IOException e) {
            LOGGER.error("Error parsing CSV line: {}", csvLine, e);
            throw new RuntimeException("Failed to parse CSV line", e);
        }
    }

}
