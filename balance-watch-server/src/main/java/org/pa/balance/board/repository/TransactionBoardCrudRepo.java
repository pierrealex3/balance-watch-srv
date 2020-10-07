package org.pa.balance.board.repository;

import org.pa.balance.board.entity.TransactionBoardEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionBoardCrudRepo extends CrudRepository<TransactionBoardEntity, Long> {

    public TransactionBoardEntity findByIdYearAndIdMonthAndIdAcctId(Integer year, Integer month, Long acctId);

    public List<TransactionBoardEntity> findAllByIdAcctId(Long acctId);
}
