package com.cubafish.controller.guest;

import com.cubafish.dto.BookingListDto;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(BookingController.BASE_PATH)
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = Logger.getLogger(BookingController.class);
    public static final String BASE_PATH = "/guest/booking";

    private final BookingListService bookingListService;
    private final BookingListRepository bookingListRepository;
    private final BookingListMapper bookingListMapper;

    @PostMapping("/create")
    public CustomResponseBody create(@RequestBody BookingBody bookingBody) {
        Map<String, Object> responseFromDataValidation = bookingListService.bookingListDataValidation(
                bookingBody.getFirstName(), bookingBody.getMiddleName(), bookingBody.getLastName(),
                bookingBody.getEmail(), bookingBody.getContact(), bookingBody.getUserConfirmation(),
                bookingBody.getTotalPrice(), bookingBody.getTotalAmount(), bookingBody.getPaymentType(),
                bookingBody.getDeliveryType(), bookingBody.getRegion(), bookingBody.getCity(), bookingBody.getAddress(),
                bookingBody.getBookingComments(), bookingBody.getBookingItems());
        String status = (String) responseFromDataValidation.get("status");
        BookingListDto bookingListDto = (BookingListDto) responseFromDataValidation.get("bookingListDto");
        if (status.equals("success")) {
            Map<String, Object> responseFromSetTextAndNumericData =
                    bookingListService.setTextAndNumericDataBeforeCreate(
                            bookingBody.getFirstName(), bookingBody.getMiddleName(), bookingBody.getLastName(),
                            bookingBody.getEmail(), bookingBody.getContact(), bookingBody.getUserConfirmation(),
                            bookingBody.getTotalPrice(), bookingBody.getTotalAmount(), bookingBody.getPaymentType(),
                            bookingBody.getDeliveryType(), bookingBody.getRegion(), bookingBody.getCity(),
                            bookingBody.getAddress(), bookingBody.getBookingComments(), bookingBody.getBookingItems(),
                            bookingListDto);
            status = (String) responseFromSetTextAndNumericData.get("status");
            BookingListDto bookingListDtoWithReceivedData =
                    (BookingListDto) responseFromSetTextAndNumericData.get("bookingListDto");
            if (status.equals("success")) {
                bookingListRepository.save(bookingListMapper.mapDtoToEntity(bookingListDtoWithReceivedData));
            }
        }
        log.info("data load: " + status);
        return new CustomResponseBody("data load", status, "no data");
    }
}
