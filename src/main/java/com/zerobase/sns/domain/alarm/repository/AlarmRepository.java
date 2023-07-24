package com.zerobase.sns.domain.alarm.repository;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import com.zerobase.sns.domain.tag.entity.Tag;
import com.zerobase.sns.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
  List<Alarm> findByUserAndTag(User user, Tag tag);
}
