package com.cubafish.controller.guest;

import com.cubafish.dto.FeedbackDto;
import com.cubafish.mapper.FeedbackMapper;
import com.cubafish.repository.FeedbackRepository;
import com.cubafish.service.FeedbackService;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(FeedbackController.BASE_PATH)
@RequiredArgsConstructor
public class FeedbackController {

    private static final Logger log = Logger.getLogger(FeedbackController.class);
    public static final String BASE_PATH = "/guest/feedback";

    private final FeedbackService feedbackService;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @PostMapping("/create")
    public CustomResponseBody create(@RequestBody FeedbackDto feedbackDto) {
        Map<String, Object> responseFromDataValidation = feedbackService.feedbackDataValidation(feedbackDto);
        String status = (String) responseFromDataValidation.get("status");
        FeedbackDto feedbackDtoAfterValidation = (FeedbackDto) responseFromDataValidation.get("feedbackDto");
        if (status.equals("success")) {
            Map<String, Object> responseFromSetDataBeforeCreate =
                    feedbackService.setDataBeforeCreate(feedbackDtoAfterValidation);
            status = (String) responseFromSetDataBeforeCreate.get("status");
            FeedbackDto feedbackDtoWithAllData = (FeedbackDto) responseFromSetDataBeforeCreate.get("feedbackDto");

            if (status.equals("success")) {
                feedbackRepository.save(feedbackMapper.mapDtoToEntity(feedbackDtoWithAllData));
            }
        }
        log.info("status: " + status);
        return new CustomResponseBody("data load", status, "no data");
    }

    @GetMapping("/all")
    public List<FeedbackDto> findAll() {
        return feedbackService.findAll();
    }
}
