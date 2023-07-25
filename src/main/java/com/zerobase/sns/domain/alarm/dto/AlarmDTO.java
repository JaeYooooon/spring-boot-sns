package com.zerobase.sns.domain.alarm.dto;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {

  private String content;
  private LocalDateTime createdTime;

  public static Page<AlarmDTO> convertToDTO(Page<Alarm> alarmList) {
    return alarmList.map(alarm -> AlarmDTO.builder()
        .content(alarm.getContent())
        .createdTime(alarm.getCreatedTime())
        .build());
  }
}
