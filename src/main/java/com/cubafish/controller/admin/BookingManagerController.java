package com.cubafish.controller.admin;

import com.cubafish.dto.BookingListDto;
import com.cubafish.entity.BookingList;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingBodyUpdate;
import com.cubafish.utils.BookingListResponseBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(BookingManagerController.BASE_PATH)
@RequiredArgsConstructor
public class BookingManagerController {

    public static final String BASE_PATH = "/admin_auth/booking";
    private final BookingListService bookingListService;
    private final BookingListRepository bookingListRepository;

    @GetMapping("/all")
    public List<BookingListResponseBody> findAll() {
        return bookingListService.findAll();
    }

    @PostMapping("update")
    public CustomResponseBody editBookingByAdmin(@RequestBody BookingBodyUpdate bookingBodyUpdate) {
        int updateStep = 0;
        String userConfirmation = "";
        String userComments = "";
        String status = "";
        if (bookingBodyUpdate.getId() != null) {
            Map<String, Object> responseFromDataValidation = bookingListService.bookingListDataValidation(
                    bookingBodyUpdate.getFirstName(), bookingBodyUpdate.getMiddleName(),
                    bookingBodyUpdate.getLastName(), bookingBodyUpdate.getEmail(), bookingBodyUpdate.getContact(),
                    userConfirmation, bookingBodyUpdate.getTotalPrice(), bookingBodyUpdate.getTotalAmount(),
                    bookingBodyUpdate.getPaymentType(), bookingBodyUpdate.getDeliveryType(),
                    bookingBodyUpdate.getRegion(), bookingBodyUpdate.getCity(), bookingBodyUpdate.getAddress(),
                    userComments, bookingBodyUpdate.getBookingItems());
            status = (String) responseFromDataValidation.get("status");
            BookingListDto bookingListDtoFromDataValidation =
                    (BookingListDto) responseFromDataValidation.get("bookingListDto");
            updateStep++;
            if (status.equals("success")) {
                Map<String, Object> responseFromDb = bookingListService.findById(bookingBodyUpdate.getId());
                BookingList bookingListFromDb = (BookingList) responseFromDb.get("bookingList");
                status = (String) responseFromDb.get("status");
                updateStep++;
                if (status.equals("success")) {
                    Map<String, Object> responseFromUpdateExitingProduct = bookingListService
                            .compensationOfMissingData(bookingBodyUpdate.getId(), bookingBodyUpdate.getFirstName(),
                                    bookingBodyUpdate.getMiddleName(), bookingBodyUpdate.getLastName(),
                                    bookingBodyUpdate.getEmail(), bookingBodyUpdate.getContact(),
                                    bookingBodyUpdate.getTotalPrice(), bookingBodyUpdate.getTotalAmount(),
                                    bookingBodyUpdate.getPaymentType(), bookingBodyUpdate.getDeliveryType(),
                                    bookingBodyUpdate.getRegion(), bookingBodyUpdate.getCity(),
                                    bookingBodyUpdate.getAddress(), userComments,
                                    bookingBodyUpdate.getBookingItems(), bookingListDtoFromDataValidation,
                                    bookingListFromDb);
                    BookingListDto dtoWithAllData =
                            (BookingListDto) responseFromUpdateExitingProduct.get("bookingListDto");
                    status = (String) responseFromUpdateExitingProduct.get("status");
                    updateStep++;
                    if (status.equals("success")) {
                        BeanUtils.copyProperties(bookingListService.create(dtoWithAllData),
                                bookingListFromDb, "bookingComments", "dateOfBooking", "userConfirmation");
                        bookingListRepository.save(bookingListFromDb);
                        status = "success";
                        updateStep++;
                    }
                }
            }
        } else {
            return new CustomResponseBody(1L, "update status", status, "no data",
                    "update step", updateStep);
        }
        return new CustomResponseBody(1L, "update status", status, "no data",
                "update step", updateStep);
    }
}
