package com.example.batch.config;

import com.example.batch.Api.TaskletApi;
import com.example.batch.Entity.ApiEntity;
import com.example.batch.Service.ApiService;
import com.example.batch.log.ChunkLog;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final ApiService apiService;
    private final EntityManagerFactory entityManagerFactory;
    private final ChunkLog chunkLog;
    private final JobRegistry jobRegistry;

    @Bean(name = "ChunkJob")
    public Job ChunkJob(JobRepository jobRepository, Step StepChunk)
    {
        return new JobBuilder("ChunkJob", jobRepository)
                .start(StepChunk)
                .build();
    }

    @Bean(name = "TaskletJob")
    public Job TaskletJob(JobRepository jobRepository, Step StepTasklet)
    {
        return new JobBuilder("TaskletJob", jobRepository)
                .start(StepTasklet)
                .build();
    }


    @Bean
    @JobScope
    public Step StepTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStepTasklet", jobRepository)
                .tasklet(new TaskletApi(apiService), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step StepChunk(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStepChunk", jobRepository)
                .<ApiEntity, ApiEntity>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .listener(chunkLog)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ApiEntity> reader()
    {
        log.info("ItemReader 실행됌");
        return new JpaPagingItemReaderBuilder<ApiEntity>()
                .pageSize(1)
                .queryString("SELECT a FROM ApiEntity a")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ApiEntity, ApiEntity> processor(){
        log.info("ItemProcessor 실행됌");
        return new ItemProcessor<ApiEntity, ApiEntity>() {
            @Override
            public ApiEntity process(ApiEntity apiEntity) throws Exception {
                //process logic
                apiEntity.setData("fixData8");
                return apiEntity;
            }
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<ApiEntity> writer(){
        log.info("ItemWriter 실행됌");
        return new JpaItemWriterBuilder<ApiEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
