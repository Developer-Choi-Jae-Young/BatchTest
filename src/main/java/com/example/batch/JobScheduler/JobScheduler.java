package com.example.batch.JobScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class JobScheduler {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    //@Scheduled(cron = "1 * * * * *")
    @Scheduled(fixedDelay = 30000)
    public void ChunkjobSchduled() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException {
        log.info("Chunk Job Schduler Start");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob("ChunkJob"), jobParameters);

        while (jobExecution.isRunning()) {
            log.info("...");
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void TaskletjobSchduled() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException {
        log.info("Tasklet Job Schduler Start");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob("TaskletJob"), jobParameters);

        while (jobExecution.isRunning()) {
            log.info("...");
        }
    }
}
