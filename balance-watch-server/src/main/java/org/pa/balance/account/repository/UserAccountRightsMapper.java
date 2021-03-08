package org.pa.balance.account.repository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pa.balance.account.UserAccountRightsPattern;
import org.pa.balance.client.model.UserAccountRights;

@Mapper
public interface UserAccountRightsMapper {

    @Mapping(target = "isOwner", expression = "java(p.isOwner())")
    @Mapping(target = "isAdmin", expression = "java(p.isAdmin())")
    @Mapping(target = "isTransfer", expression = "java(p.isTransfer())")
    @Mapping(target = "isRead", expression = "java(p.isRead())")
    public UserAccountRights fromRightPatternToDto(UserAccountRightsPattern p);


    static UserAccountRightsPattern fromDtoToRightPattern(UserAccountRights r) {
        UserAccountRightsPattern.UserAccountRightsPatternBuilder b = new UserAccountRightsPattern.UserAccountRightsPatternBuilder();
        if (r.getIsOwner())
            b.addOwner();
        if (r.getIsAdmin())
            b.addAdmin();
        if (r.getIsTransfer())
            b.addTransfer();
        if (r.getIsRead())
            b.addRead();
        return b.build();
    }
}
