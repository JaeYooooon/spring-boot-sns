package com.zerobase.sns.global.security;

import com.zerobase.sns.domain.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

  private final User user;

  public JwtUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList(); // 권한 없음
  }

  @Override
  public String getUsername() {
    return user.getUserId();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }


  @Override
  public boolean isAccountNonExpired() { // 계정의 만료 여부
    return true;
  }

  @Override
  public boolean isAccountNonLocked() { // 계정의 잠김 여부
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
    return true;
  }

  @Override
  public boolean isEnabled() { // 계정의 활성화 여부
    return true;
  }
}
