package org.pa.balance.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pa.balance.useful.TestEntities;
import org.pa.balance.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserAccountRightsRepoTest
{
    @Autowired
    UserAccountRightsRepo repo;

    @Autowired
    TestEntityManager em;

    @BeforeEach
    void setup() {
        AccountEntity ae = TestEntities.validDesjardinsAccount().build();
        UserEntity ue = TestEntities.validUser().build();

        UserAccountRightsEntityId uareid = new UserAccountRightsEntityId();
        uareid.setUser(ue);
        uareid.setAccount(ae);

        UserAccountRightsEntity uare = new UserAccountRightsEntity();
        uare.setRightPattern(1);
        uare.setId(uareid);

        em.persist(ae);
        em.persist(ue);
        em.persist(uare);
        em.flush();
    }

    @Test
    void findAllByIdUserIdIn()
    {
        List<UserAccountRightsEntity> res = repo.findAllByIdUserIdIn(Arrays.asList("momo@hotmail.com")).collect(Collectors.toList());
        assertEquals(1, res.size());
        assertEquals("momo@hotmail.com", res.get(0).getId().getUser().getId());
        assertEquals(1, res.get(0).getRightPattern());
        assertEquals(1L, res.get(0).getId().getAccount().getId());
    }
}