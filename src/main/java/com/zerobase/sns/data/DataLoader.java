package com.zerobase.sns.data;

import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.post.repository.PostRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

  private static final String[] SAMPLE_TITLES = {
      "Sample Title 1", "Sample Title 2", "Sample Title 3", "Sample Title 4", "Sample Title 5"
  };
  private static final String[] SAMPLE_CONTENTS = {
      "Sample content 1", "Sample content 2", "Sample content 3", "Sample content 4",
      "Sample content 5"
  };
  private static final Random RANDOM = new Random();
  private static final int BATCH_SIZE = 1000;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public void run(String... args) {
    if (postRepository.count() > 0) {
      System.out.println("데이터 생성 x");
      return;
    }

    List<User> users = createUsers();

    for (int i = 0; i < 1000000; i++) {
      User user = users.get(RANDOM.nextInt(users.size()));

      Post post = Post.builder()
          .title(SAMPLE_TITLES[RANDOM.nextInt(SAMPLE_TITLES.length)])
          .content(SAMPLE_CONTENTS[RANDOM.nextInt(SAMPLE_CONTENTS.length)])
          .user(user)
          .modifiedTime(LocalDateTime.now())
          .likeCount(RANDOM.nextInt(1000))
          .commentCount(RANDOM.nextInt(500))
          .build();

      entityManager.persist(post);

      if (i % BATCH_SIZE == 0 && i > 0) {
        entityManager.flush();
        entityManager.clear();
        System.out.println(i + "개의 데이터 삽입 완료");
      }
    }
    entityManager.flush();
    entityManager.clear();

    System.out.println("100만 개의 데이터 추가 완료");
  }

  private List<User> createUsers() {
    List<User> users = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      User user = User.builder()
          .userId("user" + i)
          .password(passwordEncoder.encode("password" + i))
          .nickName("nickname" + i)
          .isPrivate(false)
          .build();
      users.add(user);
    }
    return userRepository.saveAll(users);
  }
}
