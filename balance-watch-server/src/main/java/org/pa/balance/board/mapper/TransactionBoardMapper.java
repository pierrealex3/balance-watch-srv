package org.pa.balance.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pa.balance.client.model.Board;
import org.pa.balance.board.entity.TransactionBoardEntity;

@Mapper
public interface TransactionBoardMapper {

    @Mapping(target = "acctId", source = "account")
    @Mapping(target = "id", ignore = true)
    public TransactionBoardEntity fromDtoToEntity(Board dto);

    @Mapping(target = "account", source = "acctId")
    public Board fromEntityToDto(TransactionBoardEntity dto);
}
