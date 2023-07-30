package com.zerobase.sns.global.config;

import com.zerobase.sns.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
public class StoryBatchConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final StoryService storyService;

  @Bean
  public Step storyDeletionStep() {
    return stepBuilderFactory.get("storyDeletionStep")
        .tasklet((contribution, chunkContext) -> {
          storyService.deleteExpiredStory();
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Job storyDeletionJob(Step storyDeletionStep) {
    return jobBuilderFactory.get("storyDeletionJob")
        .incrementer(new RunIdIncrementer())
        .flow(storyDeletionStep)
        .end()
        .build();
  }

  @Scheduled(fixedRate = 60000)
  public void runStoryDelete() {
    storyService.deleteExpiredStory();
  }
}
