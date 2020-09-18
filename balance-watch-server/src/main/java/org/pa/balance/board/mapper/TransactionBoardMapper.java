package org.pa.balance.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pa.balance.board.entity.TransactionBoardEntity;
import org.pa.balance.client.model.Board;

@Mapper
public interface TransactionBoardMapper {

    @Mapping(target = "id.acctId", source = "account")
    @Mapping(target = "id.year", source = "year")
    @Mapping(target = "id.month", source = "month")
    public TransactionBoardEntity fromDtoToEntity(Board dto);

    @Mapping(target = "account", source = "id.acctId")
    @Mapping(target= "year", source = "id.year")
    @Mapping(target = "month", source = "id.month")
    public Board fromEntityToDto(TransactionBoardEntity dto);
}
