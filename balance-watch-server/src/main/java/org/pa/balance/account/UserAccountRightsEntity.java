package org.pa.balance.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="UserAccountRights")
@Data
@EqualsAndHashCode()
public class UserAccountRightsEntity
{
    @EmbeddedId
    private UserAccountRightsEntityId id;

    private Integer rightPattern = 0;

    public boolean isOwner() {
        return (rightPattern.intValue() & 1) == 1;
    }

    public boolean isAdmin() {
        return ((rightPattern.intValue() >> 1) & 1) == 1;
    }

    public boolean isReader() {
        return ((rightPattern.intValue() >> 2) & 1) == 1;
    }

    /**
     * All the existing rights should be assigned!
     */
    public void assignVip() {
        assignOwner().assignAdmin().assignReader();
    }

    public UserAccountRightsEntity assignOwner() {
        this.rightPattern+=1;
        return this;
    }

    public UserAccountRightsEntity assignAdmin() {
        this.rightPattern+=2;
        return this;
    }

    public UserAccountRightsEntity assignReader() {
        this.rightPattern+=4;
        return this;
    }

//    public static void main(String[] args) {
//
//        var u = new UserAccountRightsEntity();
//        u.setRightPattern(0);
//        u.assignReader();
//        System.out.println(String.format("o=%b;a=%b;r=%b", u.isOwner(), u.isAdmin(), u.isReader()));
//
//    }
}
