package org.pa.balance.transaction.repository;

import org.pa.balance.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionCrudRepo extends CrudRepository<TransactionEntity, Long> {

    public List<TransactionEntity> findByYearAndMonthAndAcctId(Integer year, Integer month, Long account);

    @Query(value = "SELECT * FROM mdsx.transactions WHERE id in (:id, :idConn) FOR UPDATE", nativeQuery = true)
    public List<TransactionEntity> selectForUpdateWithConnectedTransaction(@Param("id") Long id, @Param("idConn") Long idConn);

    @Query(value = "SELECT acct_id FROM mdsx.transactions WHERE id = :id", nativeQuery = true)
    Long selectAccountForTransactionId(@Param("id") Long transactionId);

}
