package org.pa.balance.board;

import org.mapstruct.factory.Mappers;
import org.pa.balance.board.entity.TransactionBoardEntity;
import org.pa.balance.board.mapper.TransactionBoardMapper;
import org.pa.balance.board.repository.TransactionBoardDao;
import org.pa.balance.board.validation.TransactionBoardDateLimit;
import org.pa.balance.client.model.Board;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.entity.TransactionWay;
import org.pa.balance.transaction.repository.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@Service
public class TransactionBoardDelegate {

    @Autowired
    TransactionBoardDao transactionBoardDao;

    @Autowired
    TransactionDao transactionDao;

    public Board getTransactionBoard(Integer year, Integer month, Long account) {
        TransactionBoardEntity tbe = transactionBoardDao.getTransactionBoard(year, month, account);

        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        Board b = mapper.fromEntityToDto(tbe);

        BigDecimal c = getCalculatedStartAmount(account, YearMonth.of(year, month));
        b.setStartAmtCalc(c);

        return b;
    }

    BigDecimal getCalculatedStartAmount(Long account, YearMonth targetYm) {
        Map<YearMonth, TransactionBoardEntity> tbeMap = transactionBoardDao.getTransactionBoards(account);
        List<Map.Entry<YearMonth, TransactionBoardEntity>> tbeInScopeSet = tbeMap.entrySet().stream().filter(s -> !s.getKey().isAfter(targetYm)).collect(Collectors.toList());
        Collections.reverse(tbeInScopeSet);

        Iterator<Map.Entry<YearMonth, TransactionBoardEntity>> tbeInScopeIt = tbeInScopeSet.iterator();
        Map.Entry<YearMonth, TransactionBoardEntity> tgtTb = tbeInScopeIt.next();
        if (Boolean.TRUE.equals(tgtTb.getValue().getStartAmtMan())) {
            return tgtTb.getValue().getStartAmt();
        }

        BigDecimal startAmt = BigDecimal.ZERO;
        YearMonth lastMonth = tgtTb.getKey();

        while (tbeInScopeIt.hasNext()) {
            Map.Entry<YearMonth, TransactionBoardEntity>  cand = tbeInScopeIt.next();
            boolean isOut = false;

            if (!lastMonth.minusMonths(1).equals(cand.getKey()))
                isOut = true; // is not contiguous

            if (!isOut)
            {
                lastMonth = cand.getKey();
                startAmt = startAmt.add(sumTransactionForMonth(cand.getKey(), account)).setScale(2, RoundingMode.HALF_UP);

                if (Boolean.TRUE.equals(cand.getValue().getStartAmtMan()))
                { // ends up when a manual start amount (i.e. an authority) is specified
                    startAmt = startAmt.add(cand.getValue().getStartAmt()).setScale(2, RoundingMode.HALF_UP);
                    isOut = true;
                }
            }
            if (isOut)
                break;
        }

        return startAmt;
    }

    BigDecimal sumTransactionForMonth(YearMonth ym, Long account) {
        List<TransactionEntity> tList = transactionDao.getTransactions(ym.getYear(), ym.getMonthValue(), account);
        return tList.stream().map(
                t -> {
                    BigDecimal res = t.getAmount();
                    if (t.getWay() == TransactionWay.DEBIT) {
                        res = res.negate();
                    }
                    return res;
                } ).reduce(BigDecimal.ZERO, (sum, next) -> sum.add(next).setScale(2, RoundingMode.HALF_UP) );
    }

    public void addTransactionBoard(@TransactionBoardDateLimit(message="Allow creation of young transaction boards only") Board b) {
        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        TransactionBoardEntity tbe = mapper.fromDtoToEntity(b);
        transactionBoardDao.addTransactionBoard(tbe);
    }

    public void updateTransactionBoard(Board b) {
        TransactionBoardMapper mapper = Mappers.getMapper(TransactionBoardMapper.class);
        TransactionBoardEntity tbe = mapper.fromDtoToEntity(b);
        transactionBoardDao.updateTransactionBoard(tbe);
    }


}
