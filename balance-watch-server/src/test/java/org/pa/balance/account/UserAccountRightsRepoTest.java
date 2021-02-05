package org.pa.balance.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pa.balance.useful.TestEntities;
import org.pa.balance.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

        UserEntity ue2 = TestEntities.validUser().id("popo@hotmail.com").build();

        UserAccountRightsEntityId uareid2 = new UserAccountRightsEntityId();
        uareid2.setUser(ue2);
        uareid2.setAccount(ae);

        UserAccountRightsEntity uare2 = new UserAccountRightsEntity();
        uare2.setRightPattern(3);
        uare2.setId(uareid2);

        em.persist(ae);
        em.persist(ue);
        em.persist(ue2);
        em.persist(uare);
        em.persist(uare2);
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

    @Test
    void testZonedDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 4, 11, 39);
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.of("Europe/Paris"));
        assertEquals(4, zdt.getDayOfMonth());
        assertEquals(11, zdt.getHour());
        assertEquals(39, zdt.getMinute());

        ZonedDateTime zdt2 = ZonedDateTime.of(ldt, ZoneId.of("America/Los_Angeles"));
        assertFalse(zdt.isAfter(zdt2));
    }
}