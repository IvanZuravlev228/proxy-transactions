package transaction.proxy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transaction.proxy.service.TransactionApiService;
import transaction.proxy.service.TransactionProxyService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proxy/transactions")
@RequiredArgsConstructor
public class TransactionProxyController {
    private final TransactionProxyService transactionProxyService;
    private final TransactionApiService transactionApiService;

    @GetMapping("/save/")
    public void saveTransactionsFromExternalApi(@RequestParam Map<String, String> queryParams) {
        transactionProxyService.saveReceivedTransactions(queryParams);
    }

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getTransactions(@RequestParam Map<String, String> queryParams) {
        return new ResponseEntity<>(transactionProxyService.getAndAddHashToTransaction(queryParams), HttpStatus.OK);
    }

    @GetMapping("/lastload")
    public ResponseEntity<List<Map<String, Object>>> getLastLoadTransactions() {
        return new ResponseEntity<>(transactionProxyService.getAndAddHashToLastLoad(), HttpStatus.OK);
    }

    @GetMapping("/ping")
    public ResponseEntity<Void> pingExternalApi() {
        return transactionApiService.pingApi();
    }
}
