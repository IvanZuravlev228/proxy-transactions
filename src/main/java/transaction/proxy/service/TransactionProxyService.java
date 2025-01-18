package transaction.proxy.service;

import java.util.List;
import java.util.Map;

public interface TransactionProxyService {
    void saveReceivedTransactions(Map<String, String> queryParams);

    List<Map<String, Object>> getAndAddHashToLastLoad();

    List<Map<String, Object>> getAndAddHashToTransaction(Map<String, String> queryParams);
}
