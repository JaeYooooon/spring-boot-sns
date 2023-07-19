package com.zerobase.sns.global.security;

import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    return new JwtUserDetails(user);
  }
}
