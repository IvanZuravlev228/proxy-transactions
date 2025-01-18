package transaction.proxy.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class TransactionApiService {
    private static final Logger logger = LogManager.getLogger(TransactionApiService.class);
    private static final String EXTERNAL_API_TRANSACTION_URL = "https://api.spending.gov.ua/api/v2/api/transactions/";
    private static final String EXTERNAL_API_LASTLOAD_URL = "https://api.spending.gov.ua/api/v2/api/transactions/lastload";
    private static final String EXTERNAL_API_PING_URL = "https://api.spending.gov.ua/api/v2/api/transactions/ping";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<byte[]> getTransactionsFile(Map<String, String> queryParams) {
        logger.info("Sending request to get transactions file with query params: {}", queryParams);
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    createUrlWithParams(queryParams),
                    HttpMethod.GET,
                    null,
                    byte[].class
            );
            logger.info("Received response for transactions file, status code: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while fetching transactions file with query params: {}", queryParams, e);
            throw e;
        }
    }

    public List<Map<String, Object>> getListTransactions(Map<String, String> queryParams) {
        logger.info("Sending request to get list of transactions with query params: {}", queryParams);
        try {
            ResponseEntity<List<Map<String, Object>>> response = sendParameterizedRequest(createUrlWithParams(queryParams));
            logger.info("Received response for list of transactions, status code: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error occurred while fetching list of transactions with query params: {}", queryParams, e);
            throw e;
        }
    }

    public List<Map<String, Object>> getLastLoad() {
        logger.info("Sending request to get last load data");
        try {
            ResponseEntity<List<Map<String, Object>>> response = sendParameterizedRequest(EXTERNAL_API_LASTLOAD_URL);
            logger.info("Received response for last load, status code: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error occurred while fetching last load data", e);
            throw e;
        }
    }

    public ResponseEntity<Void> pingApi() {
        logger.info("Pinging external API to check availability");
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(EXTERNAL_API_PING_URL, Void.class);
            logger.info("Received ping response, status code: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while pinging the external API", e);
            throw e;
        }
    }

    private ResponseEntity<List<Map<String, Object>>> sendParameterizedRequest(String url) {
        logger.info("Sending parameterized request to URL: {}", url);
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception e) {
            logger.error("Error occurred while sending parameterized request to URL: {}", url, e);
            throw e;
        }
    }

    private String createUrlWithParams(Map<String, String> queryParams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(EXTERNAL_API_TRANSACTION_URL);
        queryParams.forEach(uriBuilder::queryParam);
        String url = uriBuilder.toUriString();
        logger.info("Generated URL with query params: {}", url);
        return url;
    }
}
