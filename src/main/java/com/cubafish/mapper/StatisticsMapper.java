package com.cubafish.mapper;

import com.cubafish.dto.StatisticsDto;
import com.cubafish.entity.Statistics;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {
    List<StatisticsDto> mapEntitiesToDtos(List<Statistics> statistics);

    Statistics mapDtoToEntity(StatisticsDto statisticsDto);
}
