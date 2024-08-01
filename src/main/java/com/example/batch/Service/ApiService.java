package com.example.batch.Service;

import com.example.batch.Entity.ApiEntity;
import com.example.batch.Dto.ResponseDto;
import com.example.batch.Repository.ApiRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ApiRepository apiRepository;
    public List<ResponseDto> getApi()
    {
        return apiRepository.findAll().stream().map(api -> ResponseDto.of(api)).collect(Collectors.toList());
    }

    public void save(ResponseDto responseDto)
    {
        apiRepository.save(ApiEntity.builder().data(responseDto.getData()).build());
    }

    @Transactional
    @Cacheable(value = "Contents", key = "#key", cacheManager = "RedisCacheManger")
    public ResponseDto serviceApi(Long key)
    {
        return ResponseDto.of(apiRepository.findById(key).orElseThrow(()->new RuntimeException("")));
    }
}
