package org.pa.balance.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface UserAccountRightsRepo extends JpaRepository<UserAccountRightsEntity, UserAccountRightsEntityId>
{
    Stream<UserAccountRightsEntity> findByIdUserId(String userId);

    Stream<UserAccountRightsEntity> findAllByIdUserIdIn(Iterable<String> userIds);
}
