package com.zerobase.sns.domain.user.service;

import static com.zerobase.sns.global.exception.ErrorCode.ALREADY_EXIST_USER;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.sns.domain.user.dto.UserDTO;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.exception.ErrorCode;
import java.util.function.BooleanSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void concurrentJoinAttempts() throws InterruptedException {
    // 가입하려는 아이디
    String userId = "테스트용아이디123";

    // 첫 번째 가입 시도
    Thread thread1 = new Thread(() -> {
      UserDTO joinDTO = UserDTO.builder()
          .userId(userId)
          .password("비밀번호123123")
          .nickName("닉네임닉네임닉네임1")
          .isPrivate(true)
          .build();

      User newUser = null;
      try {
        newUser = userService.join(joinDTO);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      assertNotNull(newUser);
    });

    // 두 번째 가입 시도
    Thread thread2 = new Thread(() -> {
      UserDTO joinDTO = UserDTO.builder()
          .userId(userId)
          .password("비밀번호123123")
          .nickName("닉네임닉네임닉네임2")
          .isPrivate(false)
          .build();

      try {
        userService.join(joinDTO);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      try {
        fail("두 번째 가입 성공 (중복 가입)");
      } catch (CustomException e) {
        // 중복 가입으로 인한 예외 발생 확인
        assertEquals(ALREADY_EXIST_USER, e.getErrorCode());
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

  }
}