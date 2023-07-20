package com.zerobase.sns.domain.user.repository;

import com.zerobase.sns.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserId(String userId);

  boolean existsByUserId(String userId);

  boolean existsByNickName(String nickName);

  long countByNickName(String nickName);
}
