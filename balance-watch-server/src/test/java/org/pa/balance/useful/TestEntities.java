package org.pa.balance.useful;

import org.pa.balance.account.AccountEntity;
import org.pa.balance.user.UserEntity;

public class TestEntities
{
    public static UserEntity.UserEntityBuilder validUser() {
        return new UserEntity().builder().id("momo@hotmail.com");
    }

    /**
     * do NOT specify an ID or you will get:
     * javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: org.pa.balance.account.AccountEntity
     * @return
     */
    public static AccountEntity.AccountEntityBuilder validDesjardinsAccount() {
        return new AccountEntity().builder().accountNumber("DESJ-1234").description("Ops Account #1");
    }
}
