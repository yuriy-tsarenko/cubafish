package com.cubafish.controller.guest;

import com.cubafish.dto.BookingListDto;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(BookingController.BASE_PATH)
@RequiredArgsConstructor
public class BookingController {

    public static final String BASE_PATH = "/guest/booking";
    private final BookingListService bookingListService;
    private final BookingListRepository bookingListRepository;
    private final BookingListMapper bookingListMapper;

    @GetMapping("/all")
    public List<BookingListDto> findAll() {
        return bookingListService.findAll();
    }

    @PostMapping("/create")
    public CustomResponseBody create(@RequestBody BookingBody bookingBody) {
        Map<String, Object> responseFromProductDataValidation = bookingListService.bookingListDataValidation(
                bookingBody.getFirstName(), bookingBody.getMiddleName(), bookingBody.getLastName(),
                bookingBody.getEmail(), bookingBody.getContact(), bookingBody.getUserConfirmation(),
                bookingBody.getTotalPrice(), bookingBody.getTotalAmount(), bookingBody.getPaymentType(),
                bookingBody.getDeliveryType(), bookingBody.getRegion(), bookingBody.getCity(), bookingBody.getAddress(),
                bookingBody.getBookingItems());
        String status = (String) responseFromProductDataValidation.get("status");
        BookingListDto bookingListDto = (BookingListDto) responseFromProductDataValidation.get("bookingListDto");
        int step = 0;
        if (status.equals("success")) {
            Map<String, Object> responseFromSetTextAndNumericData =
                    bookingListService.setTextAndNumericDataBeforeCreate(
                            bookingBody.getFirstName(), bookingBody.getMiddleName(), bookingBody.getLastName(),
                            bookingBody.getEmail(), bookingBody.getContact(), bookingBody.getUserConfirmation(),
                            bookingBody.getTotalPrice(), bookingBody.getTotalAmount(), bookingBody.getPaymentType(),
                            bookingBody.getDeliveryType(), bookingBody.getRegion(), bookingBody.getCity(),
                            bookingBody.getAddress(), bookingBody.getBookingItems(), bookingListDto);
            status = (String) responseFromSetTextAndNumericData.get("status");
            BookingListDto bookingListDtoWithReceivedData =
                    (BookingListDto) responseFromSetTextAndNumericData.get("bookingListDto");
            step++;
            if (status.equals("success")) {
                bookingListRepository.save(bookingListMapper.mapDtoToEntity(bookingListDtoWithReceivedData));
                step++;
            }
        }
        return new CustomResponseBody(1L, "data load", status,
                "no data", "create step", step);
    }
}
