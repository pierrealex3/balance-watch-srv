package org.pa.balance.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.User;

@Mapper
public interface UserMapper
{
    UserEntity fromDtoToEntity(User dto);
    User fromEntityToDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    void fromDetachedToManaged(UserEntity detached, @MappingTarget UserEntity user);
}
