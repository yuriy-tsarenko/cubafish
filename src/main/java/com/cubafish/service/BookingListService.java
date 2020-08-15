package com.cubafish.service;

import com.cubafish.dto.BookingListDto;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.utils.BookingItem;
import lombok.Data;
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
    private final BookingListRepository bookingDataBaseRepository;
    private final BookingListMapper bookingDataBaseMapper;

    public List<BookingListDto> findAll() {
        List<BookingListDto> bookingListDtos = bookingDataBaseMapper
                .mapEntitiesToDtos(bookingDataBaseRepository.findAll());
        bookingListDtos.sort(new SortBookingListDtoById());
        return bookingListDtos;
    }

    public Map<String, Object> bookingListDataValidation(
            String firstName, String middleName, String lastName, String email,
            String contact, String userConfirmation, String totalPrice,
            String totalAmount, String paymentType, String deliveryType,
            String region, String city, String address, List<BookingItem> bookingItems
    ) {
        BookingListDto bookingListDto = new BookingListDto();
        if (firstName == null) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept first name");
        } else if (firstName.length() > 100) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the first name have more than 100 characters");
        } else if (middleName == null) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept middle name");
        } else if (middleName.length() > 100) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the middle name have more than 100 characters");
        } else if (lastName == null) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept  last name");
        } else if (lastName.length() > 100) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the last name have more than 100 characters");
        } else if (email == null) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept user email");
        } else if (email.length() > 100) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the field email have more than 100 characters");
        } else if (contact == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept user email");
        } else if (contact.length() > 50) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field email have more than 50 characters");
        } else if (userConfirmation == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept user confirmation");
        } else if (userConfirmation.length() > 200) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field user confirmation have more than 200 characters");
        } else if (paymentType == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept payment type");
        } else if (paymentType.length() > 50) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field payment type have more than 50 characters");
        } else if (deliveryType == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept delivery type");
        } else if (deliveryType.length() > 50) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field delivery type have more than 50 characters");
        } else if (region == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field region");
        } else if (region.length() > 200) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field region have more than 200 characters");
        } else if (city == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field city");
        } else if (city.length() > 200) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field city have more than 200 characters");
        } else if (address == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept field address");
        } else if (address.length() > 50) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the field address type confirmation have more than 200 characters");
        } else if (bookingItems == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept booking items");
        } else if (bookingItems.size() > 200) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the booking items have more than 200 elements");
        } else if (totalPrice == null) {
            return Map.of("bookingListDto", bookingListDto, "status",
                    "the application did not accept total price");
        } else if (!totalPrice.isEmpty()) {
            if (totalPrice.contains(",")) {
                String[] massive = totalPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                try {
                    new BigDecimal(validPrice);
                } catch (NumberFormatException e) {
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "product price should have a numeric value");
                }
            } else {
                try {
                    new BigDecimal(totalPrice);
                } catch (NumberFormatException e) {
                    return Map.of("bookingListDto", bookingListDto,
                            "status", "product price should have a numeric value");
                }
            }
        } else if (totalAmount == null) {
            return Map.of("bookingListDto", bookingListDto,
                    "status", "the application did not accept any amount of products");
        } else if (!totalAmount.isEmpty()) {
            if (totalAmount.contains(",") || totalAmount.contains(".")) {
                return Map.of("bookingListDto", bookingListDto,
                        "status", "the amount of products can not have <<,>> or <<.>>");
            } else {
                try {
                    Integer.valueOf(totalAmount);
                } catch (NumberFormatException e) {
                    return Map.of("bookingListDto", bookingListDto, "status",
                            "the amount of products should have a numeric value");
                }
            }
        }
        return Map.of("bookingListDto", bookingListDto, "status", "success");
    }

    public Map<String, Object> setTextAndNumericDataBeforeCreate(
            String firstName, String middleName, String lastName, String email,
            String contact, String userConfirmation, String totalPrice,
            String totalAmount, String paymentType, String deliveryType,
            String region, String city, String address, List<BookingItem> bookingItems,
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
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/hh:mm");
        bookingListDto.setDateOfBooking(java.sql.Date.valueOf(dateFormat.format(date)));

        StringBuilder itemsBuilder = new StringBuilder();
        for (BookingItem item : bookingItems) {
            itemsBuilder.append(item.getDescription());
            itemsBuilder.append("-");
            itemsBuilder.append(item.getTotalAmount());
            itemsBuilder.append("-");
            itemsBuilder.append(item.getProductPrice());
            itemsBuilder.append("*");
        }

        bookingListDto.setBookingItems(itemsBuilder.toString());


        if (totalAmount.isEmpty()) {
            bookingListDto.setTotalAmount(Integer.valueOf("0"));
        } else {
            try {
                Integer totalAmountConverted = Integer.valueOf(totalAmount.trim());
                bookingListDto.setTotalAmount(totalAmountConverted);
            } catch (NumberFormatException e) {
                return Map.of("productDto", bookingListDto,
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
                return Map.of("productDto", bookingListDto, "status",
                        "product price should have a numeric value");
            }
        } else {
            try {
                BigDecimal convertToBigDecimal = new BigDecimal(totalPrice);
                bookingListDto.setTotalPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                return Map.of("productDto", bookingListDto,
                        "status", "product price should have a numeric value");
            }
        }
        return Map.of("productDto", bookingListDto, "status", "success");
    }

    static class SortBookingListDtoById implements Comparator<BookingListDto> {
        @Override
        public int compare(BookingListDto bookingListDto, BookingListDto bookingListDto1) {
            return bookingListDto.getId().compareTo(bookingListDto1.getId());
        }
    }
}
