package com.cubafish.mapper;

import com.cubafish.dto.FeedbackDto;
import com.cubafish.entity.Feedback;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    List<FeedbackDto> mapEntitiesToDtos(List<Feedback> feedbacks);

    Feedback mapDtoToEntity(FeedbackDto feedbackDto);
}
