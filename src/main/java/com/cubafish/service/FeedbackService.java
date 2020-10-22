package com.cubafish.service;

import com.cubafish.dto.FeedbackDto;
import com.cubafish.mapper.FeedbackMapper;
import com.cubafish.repository.FeedbackRepository;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Data
public class FeedbackService {

    private static final Logger log = Logger.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    public List<FeedbackDto> findAll() {
        List<FeedbackDto> feedbackDtos = feedbackMapper
                .mapEntitiesToDtos(feedbackRepository.findAll());
        feedbackDtos.sort(new SortFeedbackDtoById());
        return feedbackDtos;
    }

    public Map<String, Object> feedbackDataValidation(FeedbackDto feedbackDto) {
        if (feedbackDto.getUserName() == null) {
            log.error("status: the application did not accept user name");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the application did not accept user name");
        } else if (feedbackDto.getUserName().length() > 100) {
            log.error("status: the user name have more than 100 characters " + feedbackDto.getUserName());
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the user name have more than 100 characters");
        } else if (feedbackDto.getUserLastName() == null) {
            log.error("status: the application did not accept user's last name");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the application did not accept user's last name");
        } else if (feedbackDto.getUserLastName().length() > 100) {
            log.error("status: the user's last name have more than 100 characters");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the user's last name have more than 100 characters");
        } else if (feedbackDto.getRecommendation() == null) {
            log.error("status: the application did not accept user's recommendation");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the application did not accept user's recommendation");
        } else if (feedbackDto.getComment() == null) {
            log.error("status: the application did not accept user's comment");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the application did not accept user's comment");
        } else if (feedbackDto.getComment().length() > 1000) {
            log.error("status: the user's comment have more than 1000 characters");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the user's comment have more than 1000 characters");
        } else if (feedbackDto.getMark() == null) {
            log.error("status: the application did not accept user's mark");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the application did not accept user's mark");
        } else if ((feedbackDto.getMark() > 10) || (feedbackDto.getMark() < 0)) {
            log.error("status: the user's mark  more than 10 or less then 0");
            return Map.of("feedbackDto", feedbackDto,
                    "status", "the user's mark more than 10 or less then 0");
        }
        log.info("status: success");
        return Map.of("feedbackDto", feedbackDto, "status", "success");
    }


    public Map<String, Object> setDataBeforeCreate(FeedbackDto feedbackDto) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        feedbackDto.setDateOfComment(dateFormat.format(currentDate));
        log.info("status: success");
        return Map.of("feedbackDto", feedbackDto,
                "status", "success");
    }

    static class SortFeedbackDtoById implements Comparator<FeedbackDto> {
        @Override
        public int compare(FeedbackDto feedbackDto, FeedbackDto feedbackDto1) {
            return feedbackDto1.getId().compareTo(feedbackDto.getId());
        }
    }
}
