package com.cubafish.service;

import com.cubafish.dto.BookingItemDto;
import com.cubafish.dto.BookingListDto;
import com.cubafish.entity.BookingList;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.utils.BookingListResponseBody;
import lombok.Data;
import org.apache.log4j.Logger;
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
public class BookingListService {
    private static final Logger log = Logger.getLogger(BookingListService.class);

    private final BookingListRepository bookingListRepository;

    private final BookingListMapper bookingListMapper;

    private final BookingItemService bookingItemService;


    public List<BookingListResponseBody> findAll() {
        List<BookingListDto> bookingListDtos = bookingListMapper
                .mapEntitiesToDtos(bookingListRepository.findAll());
        bookingListDtos.sort(new SortBookingListDtoById());
        return bookingItemService.mapBookingListDtosToBookingListResponseBodies(bookingListDtos);
    }

    public Map<String, Object> findById(Long id) {
        BookingList bookingList = bookingListRepository.findBookingListById(id);
        return Map.of("bookingList", bookingList, "status", "success");
    }

    public BookingList create(BookingListDto bookingListDto) {
        return bookingListMapper.mapDtoToEntity(bookingListDto);
    }

    public Map<String, Object> bookingListDataValidation(
            String firstName, String middleName, String lastName, String email,
            String contact, String userConfirmation, String totalPrice,
            String totalAmount, String paymentType, String deliveryType,
            String region, String city, String address, String bookingComments, List<BookingItemDto> bookingItems
    ) {
        BookingListDto bookingListDto = new BookingListDto();
        if (firstName == null) {
            log.error("status: the application did not accept first name");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept first name");
        } else if (firstName.length() > 100) {
            log.error("user: " + firstName + " " + lastName + " " + contact + " status: the first name have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the first name have more than 100 characters");
        } else if (middleName == null) {
            log.error("user: " + firstName + " " + lastName + " " + contact + " status: the application did not accept middle name");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept middle name");
        } else if (middleName.length() > 100) {
            log.error("user: " + firstName + " " + lastName + " " + contact + " status: the middle name have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the middle name have more than 100 characters");
        } else if (lastName == null) {
            log.error("user: " + firstName + " " + contact + " status: the application did not accept last name");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept last name");
        } else if (lastName.length() > 100) {
            log.error(firstName + " " + lastName + " " + contact + " status: the last name have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the last name have more than 100 characters");
        } else if (email == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept user email");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept user email");
        } else if (email.length() > 100) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field email have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the field email have more than 100 characters");
        } else if (contact == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept user contact");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept user contact");
        } else if (contact.length() > 50) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field contact have more than 50 characters " + contact);
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field contact have more than 50 characters");
        } else if (userConfirmation == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept user confirmation");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept user confirmation");
        } else if (userConfirmation.length() > 250) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field user confirmation have more than 250 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field user confirmation have more than 250 characters");
        } else if (paymentType == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept payment type" + contact);
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept payment type");
        } else if (paymentType.length() > 100) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field payment type have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field payment type have more than 100 characters");
        } else if (deliveryType == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept delivery type");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept delivery type");
        } else if (deliveryType.length() > 100) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field delivery type have more than 100 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field delivery type have more than 100 characters");
        } else if (region == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept field region");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field region");
        } else if (region.length() > 250) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field region have more than 250 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field region have more than 250 characters");
        } else if (city == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept field city");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field city");
        } else if (city.length() > 250) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field city have more than 250 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field city have more than 250 characters");
        } else if (address == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept field address");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field address");
        } else if (address.length() > 250) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field address have more than 250 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field address have more than 250 characters");
        } else if (bookingComments == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept field booking comments");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field booking comments");
        } else if (bookingComments.length() > 400) {
            log.error(firstName + " " + lastName + " " + contact + " status: the field booking comments have more than 400 characters");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field booking comments have more than 400 characters");
        } else if (bookingItems == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept booking items");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept booking items");
        } else if (bookingItems.size() > 200) {
            log.error(firstName + " " + lastName + " " + contact + " status: the booking items have more than 200 elements");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the booking items have more than 200 elements");
        } else if (totalPrice == null) {
            log.error(firstName + " " + lastName + " " + contact + " status: the application did not accept total price");
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept total price");
        } else if (!totalPrice.isEmpty()) {
            if (totalPrice.contains(",")) {
                String[] massive = totalPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                try {
                    new BigDecimal(validPrice);
                } catch (NumberFormatException e) {
                    log.error(firstName + " " + lastName + " " + contact + e);
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "product price should have a numeric value");
                }
            } else {
                try {
                    new BigDecimal(totalPrice);
                } catch (NumberFormatException e) {
                    log.error(firstName + " " + lastName + " " + contact + e);
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "product price should have a numeric value");
                }
            }
        } else if (totalAmount == null) {
            log.error(firstName + " " + lastName + " " + contact + " the application did not accept any amount of products");
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept any amount of products");
        } else if (!totalAmount.isEmpty()) {
            if (totalAmount.contains(",") || totalAmount.contains(".")) {
                log.error(firstName + " " + lastName + " " + contact + " the amount of products can not have <<,>> or <<.>>");
                return Map.of("bookingListDto", bookingListDto,
                        "status", "the amount of products can not have <<,>> or <<.>>");
            } else {
                try {
                    Integer.valueOf(totalAmount);
                } catch (NumberFormatException e) {
                    log.error(firstName + " " + lastName + " " + contact + e);
                    return Map.of("bookingListDto", bookingListDto, "status",
                            "the amount of products should have a numeric value");
                }
            }
        }
        log.info(firstName + " " + lastName + " " + contact + "status: " + "success");
        return Map.of("bookingListDto", bookingListDto, "status", "success");
    }

    public Map<String, Object> setTextAndNumericDataBeforeCreate(
            String firstName, String middleName, String lastName, String email,
            String contact, String userConfirmation, String totalPrice,
            String totalAmount, String paymentType, String deliveryType,
            String region, String city, String address, String bookingComments, List<BookingItemDto> bookingItems,
            BookingListDto bookingListDto) {

        bookingListDto.setFirstName(firstName.trim());
        bookingListDto.setMiddleName(middleName.trim());
        bookingListDto.setLastName(lastName.trim());
        bookingListDto.setEmail(email.trim());
        bookingListDto.setContact(contact.trim());
        bookingListDto.setUserConfirmation(userConfirmation.trim());
        bookingListDto.setPaymentType(paymentType.trim());
        bookingListDto.setDeliveryType(deliveryType.trim());
        bookingListDto.setRegion(region.trim());
        bookingListDto.setCity(city.trim());
        bookingListDto.setAddress(address.trim());
        bookingListDto.setBookingComments(bookingComments.trim());
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bookingListDto.setDateOfBooking(dateFormat.format(currentDate));

        Map<String, Object> responseFromItemService = bookingItemService.saveItemsFromList(bookingItems);
        String bookingItemStringForDb = (String) responseFromItemService.get("result");
        String status = (String) responseFromItemService.get("status");
        if (status.equals("success")) {
            bookingListDto.setBookingItems(bookingItemStringForDb);
        } else {
            log.error("status: " + status);
            return Map.of("bookingListDto", bookingListDto,
                    "status", status);
        }

        if (totalAmount == null) {
            bookingListDto.setTotalAmount(bookingItems.size());
        } else if (totalAmount.isEmpty()) {
            bookingListDto.setTotalAmount(bookingItems.size());
        } else {
            try {
                Integer totalAmountConverted = Integer.valueOf(totalAmount.trim());
                bookingListDto.setTotalAmount(totalAmountConverted);
            } catch (NumberFormatException e) {
                log.error(e);
                return Map.of("bookingListDto", bookingListDto,
                        "status", "the amount of products should have a numeric value");
            }
        }

        if (totalPrice.isEmpty()) {
            bookingListDto.setTotalPrice(new BigDecimal("0"));
        } else if (totalPrice.contains(",")) {
            String[] massive = totalPrice.split(",");
            String validPrice = massive[0].concat(".").concat(massive[1]);
            try {
                BigDecimal convertToBigDecimal = new BigDecimal(validPrice);
                bookingListDto.setTotalPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                log.error(e);
                return Map.of("bookingListDto", bookingListDto, "status",
                        "product price should have a numeric value");
            }
        } else {
            try {
                BigDecimal convertToBigDecimal = new BigDecimal(totalPrice);
                bookingListDto.setTotalPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                log.error(e);
                return Map.of("bookingListDto", bookingListDto,
                        "status", "product price should have a numeric value");
            }
        }
        log.info("status: " + "success");
        return Map.of("bookingListDto", bookingListDto, "status", "success");
    }

    public Map<String, Object> compensationOfMissingData(
            Long id, String firstName, String middleName, String lastName, String email,
            String contact, String totalPrice, String totalAmount, String paymentType, String deliveryType,
            String region, String city, String address, String bookingComments, List<BookingItemDto> bookingItems,
            BookingListDto bookingListDto, BookingList exiting) {

        if (id != null) {
            bookingListDto.setId(id);
            if (firstName.isEmpty()) {
                bookingListDto.setFirstName(exiting.getFirstName());
            } else {
                bookingListDto.setFirstName(firstName.trim());
            }
            if (middleName.isEmpty()) {
                bookingListDto.setMiddleName(exiting.getMiddleName());
            } else {
                bookingListDto.setMiddleName(middleName.trim());
            }
            if (lastName.isEmpty()) {
                bookingListDto.setLastName(exiting.getLastName());
            } else {
                bookingListDto.setLastName(lastName.trim());
            }
            if (email.isEmpty()) {
                bookingListDto.setEmail(exiting.getEmail());
            } else {
                bookingListDto.setEmail(email.trim());
            }
            if (contact.isEmpty()) {
                bookingListDto.setContact(exiting.getContact());
            } else {
                bookingListDto.setContact(contact.trim());
            }

            if (totalAmount.isEmpty()) {
                bookingListDto.setTotalAmount(exiting.getTotalAmount());
            } else {
                try {
                    Integer totalAmountConverted = Integer.valueOf(totalAmount.trim());
                    bookingListDto.setTotalAmount(totalAmountConverted);
                } catch (NumberFormatException e) {
                    log.error(e);
                    return Map.of("bookingListDto", bookingListDto, "status",
                            "the amount of product items should have a numeric value");
                }
            }

            if (totalPrice.isEmpty()) {
                bookingListDto.setTotalPrice(exiting.getTotalPrice());
            } else if (totalPrice.contains(",")) {
                String[] massive = totalPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                BigDecimal convertToBigDecimal = new BigDecimal(validPrice);
                try {
                    bookingListDto.setTotalPrice(convertToBigDecimal);
                } catch (NumberFormatException e) {
                    log.error(e);
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "total price should have a numeric value");
                }
            } else {
                try {
                    bookingListDto.setTotalPrice(new BigDecimal(totalPrice));
                } catch (NumberFormatException e) {
                    log.error(e);
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "total price should have a numeric value");
                }
            }

            if (paymentType.isEmpty()) {
                bookingListDto.setPaymentType(exiting.getPaymentType());
            } else {
                bookingListDto.setPaymentType(paymentType.trim());
            }
            if (deliveryType.isEmpty()) {
                bookingListDto.setDeliveryType(exiting.getDeliveryType());
            } else {
                bookingListDto.setDeliveryType(deliveryType.trim());
            }
            if (region.isEmpty()) {
                bookingListDto.setRegion(exiting.getRegion());
            } else {
                bookingListDto.setRegion(region.trim());
            }
            if (city.isEmpty()) {
                bookingListDto.setCity(exiting.getCity());
            } else {
                bookingListDto.setCity(city.trim());
            }
            if (address.isEmpty()) {
                bookingListDto.setAddress(exiting.getAddress());
            } else {
                bookingListDto.setAddress(address.trim());
            }
            if (bookingComments.isEmpty()) {
                bookingListDto.setBookingComments(exiting.getBookingComments());
            } else {
                bookingListDto.setBookingComments(bookingComments.trim());
            }
            if (bookingItems.isEmpty()) {
                bookingListDto.setBookingItems(exiting.getBookingItems());
            } else {
                Map<String, Object> responseFromUpdateExitingItemsFromList = bookingItemService
                        .updateExitingItemsFromList(exiting.getBookingItems(), bookingItems);
                String bookingItemStringForDb = (String) responseFromUpdateExitingItemsFromList.get("result");
                String status = (String) responseFromUpdateExitingItemsFromList.get("status");
                if (status.equals("success")) {
                    bookingListDto.setBookingItems(bookingItemStringForDb);
                } else {
                    log.error("status: " + status);
                    return Map.of("bookingListDto", bookingListDto,
                            "status", status);
                }
            }
            log.info("status: " + "success");
            return Map.of("bookingListDto", bookingListDto, "status", "success");
        } else {
            log.error("status: " + "ID of edited booking list not received!");
            return Map.of("bookingListDto", bookingListDto, "status", "ID of edited booking list not received!");
        }
    }

    static class SortBookingListDtoById implements Comparator<BookingListDto> {
        @Override
        public int compare(BookingListDto bookingListDto, BookingListDto bookingListDto1) {
            return bookingListDto.getId().compareTo(bookingListDto1.getId());
        }
    }
}
