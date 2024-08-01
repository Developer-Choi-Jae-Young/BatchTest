package com.example.batch.Api;

import com.example.batch.Dto.ResponseDto;
import com.example.batch.Service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskletApi implements Tasklet {
    private final ApiService apiService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        for (int idx = 0; idx < 5; idx++) {
            apiService.save(ResponseDto.builder().data("Data" + idx).build());
        }

        return RepeatStatus.FINISHED;
    }
}
