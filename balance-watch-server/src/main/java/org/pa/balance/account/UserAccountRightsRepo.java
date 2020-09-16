package org.pa.balance.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface UserAccountRightsRepo extends JpaRepository<UserAccountRightsEntity, Long>
{
    Stream<UserAccountRightsEntity> findByIdUserId(Long userId);
}
