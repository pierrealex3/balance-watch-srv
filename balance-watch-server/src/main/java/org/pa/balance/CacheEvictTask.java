package org.pa.balance;

import org.pa.balance.user.UserDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheEvictTask
{
    @Autowired
    UserDelegate userDelegate;

    @Scheduled(initialDelayString = "${user.cache.clear.init.delay}", fixedDelayString = "${user.cache.clear.recurrent.delay}")
    public void clearLongTermCaches() {
        userDelegate.clearUserCache();
    }
}
