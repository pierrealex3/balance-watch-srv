package org.pa.balance.service;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.Board;
import org.pa.balance.model.TransactionBoardEntity;
import org.pa.balance.repository.TransactionBoardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class TransactionBoardDelegate {

    @Autowired
    TransactionBoardDao transactionBoardDao;

    public Board getTransactionBoard(Integer year, Integer month, String account) {
        TransactionBoardEntity tbe = transactionBoardDao.getTransactionBoard(year, month, account);

        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        Board b = mapper.fromEntityToDto(tbe);

        return b;
    }

    public Long addTransactionBoard(@TransactionBoardDateLimit(message="Allow creation of young transaction boards only") Board b) {
        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        TransactionBoardEntity tbe = mapper.fromDtoToEntity(b);

        TransactionBoardEntity tbex = transactionBoardDao.addTransactionBoard(tbe);

        return tbex.getId();
    }

    public Long updateTransactionBoard(Board b) {
        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        TransactionBoardEntity tbe = mapper.fromDtoToEntity(b);

        TransactionBoardEntity tbex = transactionBoardDao.updateTransactionBoard(tbe);
        return tbex.getId();
    }


}
