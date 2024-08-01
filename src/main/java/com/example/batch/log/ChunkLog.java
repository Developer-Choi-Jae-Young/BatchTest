package com.example.batch.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChunkLog implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context)
    {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        log.info("#### beforeChunk : " + stepExecution.getReadCount());
    }

    @Override
    public void afterChunk(ChunkContext context)
    {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        log.info("#### afterChunk : " + stepExecution.getCommitCount());
    }
}
