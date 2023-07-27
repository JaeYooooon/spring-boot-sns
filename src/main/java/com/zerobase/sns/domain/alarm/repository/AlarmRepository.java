package com.zerobase.sns.domain.alarm.repository;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import com.zerobase.sns.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

  Page<Alarm> findByUser(User user, Pageable pageable);
}
