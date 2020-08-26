package com.cubafish.service;

import com.cubafish.dto.StatisticsDto;
import com.cubafish.entity.Statistics;
import com.cubafish.mapper.StatisticsMapper;
import com.cubafish.repository.StatisticsRepository;
import com.cubafish.utils.BookingListResponseBody;
import com.cubafish.utils.CustomRequestBody;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Data
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final StatisticsMapper statisticsMapper;

    public List<StatisticsDto> findAll() {
        List<StatisticsDto> statisticsDtos = statisticsMapper
                .mapEntitiesToDtos(statisticsRepository.findAll());
        statisticsDtos.sort(new SortStatisticsDtoById());
        return statisticsDtos;
    }

    public Map<String, Object> updateOrSaveStatistics(BookingListResponseBody bookingListResponseBody,
                                                      CustomRequestBody customRequestBody) {


        StatisticsDto statisticsDto = new StatisticsDto();
        Statistics statisticsFromDb = new Statistics();
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormatForReportingPeriod = new SimpleDateFormat("yyyy-MM");
        String[] parsedDateLine = dateFormat.format(currentDate).split("-");
        int day = Integer.parseInt(parsedDateLine[2]);
        boolean flag = false;
        if ((statisticsRepository.existsStatisticsByReportingPeriod(dateFormatForReportingPeriod
                .format(currentDate))) && (day != 28)) {
            statisticsFromDb = statisticsRepository.findStatisticsByReportingPeriod(dateFormatForReportingPeriod
                    .format(currentDate));

            if (customRequestBody.getCommunicationKey().equals("approve booking")) {
                statisticsDto.setUpdateDate(dateFormat.format(currentDate));
                statisticsDto.setTotalPrice(bookingListResponseBody.getTotalPrice()
                        .add(statisticsFromDb.getTotalPrice()));
                statisticsDto.setTotalAmount(Long.valueOf(bookingListResponseBody
                        .getTotalAmount()) + statisticsFromDb.getTotalAmount());
                statisticsDto.setBookingAmount(statisticsFromDb.getBookingAmount() + 1L);
                BeanUtils.copyProperties(statisticsMapper.mapDtoToEntity(statisticsDto), statisticsFromDb,
                        "id", "reportingPeriod", "canceledBookingAmount", "canceledBookingPrice");
                flag = true;

            } else if (customRequestBody.getCommunicationKey().equals("cancel booking")) {
                statisticsDto.setUpdateDate(dateFormat.format(currentDate));
                statisticsDto.setCanceledBookingPrice(bookingListResponseBody.getTotalPrice()
                        .add(statisticsFromDb.getCanceledBookingPrice()));
                statisticsDto.setCanceledBookingAmount(statisticsFromDb.getCanceledBookingAmount() + 1L);
                BeanUtils.copyProperties(statisticsMapper.mapDtoToEntity(statisticsDto), statisticsFromDb,
                        "id", "reportingPeriod", "totalPrice", "totalAmount", "bookingAmount");
                flag = true;
            }

        } else if ((!statisticsRepository.existsStatisticsByReportingPeriod(dateFormatForReportingPeriod
                .format(currentDate))) || (day == 28)) {

            if (customRequestBody.getCommunicationKey().equals("approve booking")) {
                statisticsDto.setUpdateDate(dateFormat.format(currentDate));
                statisticsDto.setReportingPeriod(dateFormatForReportingPeriod.format(currentDate));
                statisticsDto.setTotalPrice(bookingListResponseBody.getTotalPrice());
                statisticsDto.setTotalAmount(Long.valueOf(bookingListResponseBody.getTotalAmount()));
                statisticsDto.setBookingAmount(1L);
                statisticsDto.setCanceledBookingPrice(BigDecimal.ZERO);
                statisticsDto.setCanceledBookingAmount(0L);

            } else if (customRequestBody.getCommunicationKey().equals("cancel booking")) {
                statisticsDto.setUpdateDate(dateFormat.format(currentDate));
                statisticsDto.setReportingPeriod(dateFormatForReportingPeriod.format(currentDate));
                statisticsDto.setTotalPrice(BigDecimal.ZERO);
                statisticsDto.setTotalAmount(0L);
                statisticsDto.setBookingAmount(0L);
                statisticsDto.setCanceledBookingPrice(bookingListResponseBody.getTotalPrice());
                statisticsDto.setCanceledBookingAmount(1L);
            }

        }
        return Map.of("status", "success", "statisticsFromDb", statisticsFromDb,
                "statisticsDto", statisticsDto, "flag", flag);
    }

    static class SortStatisticsDtoById implements Comparator<StatisticsDto> {
        @Override
        public int compare(StatisticsDto statisticsDto, StatisticsDto statisticsDto1) {
            return statisticsDto.getId().compareTo(statisticsDto1.getId());
        }
    }
}
