package com.example.batch.Dto;

import com.example.batch.Entity.ApiEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDto {
    private Long id;

    private String data;

    public static ResponseDto of(ApiEntity apiEntity)
    {
        return ResponseDto.builder().id(apiEntity.getId()).data(apiEntity.getData()).build();
    }
}
