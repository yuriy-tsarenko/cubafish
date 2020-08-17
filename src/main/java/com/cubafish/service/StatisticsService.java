package com.cubafish.service;

import com.cubafish.dto.BookingListDto;
import com.cubafish.dto.StatisticsDto;
import com.cubafish.mapper.StatisticsMapper;
import com.cubafish.repository.StatisticsRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Data
public class StatisticsService {
    private final StatisticsRepository bookingDataBaseRepository;
    private final StatisticsMapper bookingDataBaseMapper;

    public List<StatisticsDto> findAll() {
        List<StatisticsDto> statisticsDtos = bookingDataBaseMapper
                .mapEntitiesToDtos(bookingDataBaseRepository.findAll());
        statisticsDtos.sort(new SortStatisticsDtoById());
        return statisticsDtos;
    }

    static class SortStatisticsDtoById implements Comparator<StatisticsDto> {
        @Override
        public int compare(StatisticsDto statisticsDto, StatisticsDto statisticsDto1) {
            return statisticsDto.getId().compareTo(statisticsDto1.getId());
        }
    }
}
