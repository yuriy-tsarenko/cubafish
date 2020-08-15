package com.cubafish.controller.guest;

import com.cubafish.dto.BookingListDto;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingItem;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public CustomResponseBody create(
            @RequestParam(required = false, name = "firstnameUser") String firstName,
            @RequestParam(required = false, name = "middleNameUser") String middleName,
            @RequestParam(required = false, name = "lastNameUser") String lastName,
            @RequestParam(required = false, name = "emailNameUser") String email,
            @RequestParam(required = false, name = "phoneNameUser") String contact,
            @RequestParam(required = false, name = "confirmBooking") String userConfirmation,
            @RequestParam(required = false, name = "totalProductPrice") String totalPrice,
            @RequestParam(required = false, name = "totalItemsAmount") String totalAmount,
            @RequestParam(required = false, name = "paymentType") String paymentType,
            @RequestParam(required = false, name = "deliveryType") String deliveryType,
            @RequestParam(required = false, name = "regionUser") String region,
            @RequestParam(required = false, name = "cityUser") String city,
            @RequestParam(required = false, name = "addressUser") String address,
            @RequestParam(required = false, name = "setOfProducts") List<BookingItem> bookingItems
    ) {
        Map<String, Object> responseFromProductDataValidation = bookingListService.bookingListDataValidation(
                firstName, middleName, lastName, email, contact, userConfirmation, totalPrice,
                totalAmount, paymentType, deliveryType, region, city, address, bookingItems);
        String status = (String) responseFromProductDataValidation.get("status");
        BookingListDto bookingListDto = (BookingListDto) responseFromProductDataValidation.get("bookingListDto");
        int step = 0;
        if (status.equals("success")) {
            Map<String, Object> responseFromSetTextAndNumericData =
                    bookingListService.setTextAndNumericDataBeforeCreate(
                    firstName, middleName, lastName, email, contact, userConfirmation, totalPrice, totalAmount,
                    paymentType, deliveryType, region, city, address, bookingItems, bookingListDto);
            status = (String) responseFromSetTextAndNumericData.get("status");
            BookingListDto bookingListDtoWithReceivedData =
                    (BookingListDto) responseFromSetTextAndNumericData.get("bookingListDto");
            step++;
            if (status.equals("success")) {
                bookingListRepository.save(bookingListMapper.mapDtoToEntity(bookingListDtoWithReceivedData));
                step++;
            }
        }
        return new CustomResponseBody(1L, "data load", "status",
                "no data", "create step", step);
    }
}
