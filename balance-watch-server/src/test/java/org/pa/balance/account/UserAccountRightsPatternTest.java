package org.pa.balance.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserAccountRightsPatternTest
{
    @Test
    void testVipAccountRights_FromBuilder() {
        var uare = new UserAccountRightsPattern.UserAccountRightsPatternBuilder().addVip().build();
        assertVip(uare);
    }

    @Test
    void testVipAccountRights_FromBitMask() {
        var uare = UserAccountRightsPattern.from(1 + 2 + 4 + 8);
        assertVip(uare);
    }

    @Test
    void testReadOnlyAccountRights_FromBuilder() {
        var uare = new UserAccountRightsPattern.UserAccountRightsPatternBuilder().addRead().build();
        assertReadOnly(uare);
    }

    @Test
    void testReadOnlyAccountRights_FromBitMask() {
        var uare = UserAccountRightsPattern.from(8);
        assertReadOnly(uare);
    }

    private void assertVip(UserAccountRightsPattern uare) {
        assertTrue( uare.isAdmin() && uare.isTransfer() && uare.isOwner() && uare.isRead() );
    }

    private void assertReadOnly(UserAccountRightsPattern uare) {
        assertTrue(uare.isRead());
        assertFalse(uare.isAdmin());
        assertFalse(uare.isOwner());
        assertFalse(uare.isTransfer());
    }

}