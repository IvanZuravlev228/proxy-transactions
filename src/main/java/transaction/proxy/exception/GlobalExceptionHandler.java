package transaction.proxy.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        String errorMessage = ex.getResponseBodyAsString();
        HttpStatusCode statusCode = ex.getStatusCode();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorMap = new HashMap<>();

        try {
            errorMap = objectMapper.readValue(errorMessage, Map.class);
        } catch (JsonProcessingException e) {
            errorMap.put("apiErrorMessage", errorMessage);
        }

        logger.info("Handled HttpClientErrorException with status code: {}", statusCode);  // Логування статусу
        return new ResponseEntity<>(errorMap, statusCode);
    }
}