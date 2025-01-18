package transaction.proxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transaction.proxy.entity.TransactionProxy;

public interface TransactionProxyRepository extends JpaRepository<TransactionProxy, Long> {
}
