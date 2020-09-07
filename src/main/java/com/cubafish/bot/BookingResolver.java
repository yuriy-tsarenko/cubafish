package com.cubafish.bot;

import com.cubafish.dto.BookingItemDto;
import com.cubafish.dto.BookingListDto;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingListResponseBody;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Data
public class BookingResolver {
    private final BookingListService bookingListService;

    public Map<String, Object> getMessageForAdmin(Integer howManyBookings) {
        StringBuilder messageBuilder = new StringBuilder();
        List<BookingListResponseBody> bookingListResponseBodies = bookingListService.findAll();
        bookingListResponseBodies.sort(new SortBookingListResponseBodyById());
        int totalAmount = bookingListResponseBodies.size();
        int id = 1;
        if ((totalAmount > 0) & (howManyBookings > 0)) {
            BookingListResponseBody bookingListResponseBody = bookingListResponseBodies
                    .get(totalAmount - howManyBookings);
            messageBuilder.append("заказ #");
            messageBuilder.append(bookingListResponseBody.getId());
            messageBuilder.append("\n");
            messageBuilder.append(bookingListResponseBody.getDateOfBooking());
            messageBuilder.append("\n");
            messageBuilder.append(bookingListResponseBody.getFirstName());
            messageBuilder.append(" ");
            messageBuilder.append(bookingListResponseBody.getLastName());
            messageBuilder.append("\n");
            messageBuilder.append("Адрес: ");
            messageBuilder.append(bookingListResponseBody.getAddress());
            messageBuilder.append("\n");
            messageBuilder.append(bookingListResponseBody.getCity());
            messageBuilder.append("\n");
            messageBuilder.append(bookingListResponseBody.getRegion());
            messageBuilder.append("\n");
            messageBuilder.append("Номер телефона: ");
            messageBuilder.append(bookingListResponseBody.getContact());
            messageBuilder.append("\n");
            messageBuilder.append("Наименование товаров: ");
            messageBuilder.append("\n");
            for (BookingItemDto item : bookingListResponseBody.getBookingItems()) {
                messageBuilder.append(id);
                messageBuilder.append(". ");
                messageBuilder.append(item.getDescription());
                messageBuilder.append(" ");
                messageBuilder.append(item.getItemAmount());
                messageBuilder.append("шт. ");
                messageBuilder.append(item.getProductPrice());
                messageBuilder.append("грн. ");
                messageBuilder.append("\n");
                id++;
            }
            messageBuilder.append("Сумма заказа: ");
            messageBuilder.append(bookingListResponseBody.getTotalPrice());
            messageBuilder.append("грн. ");
        }
        return Map.of("bookingMessage", messageBuilder.toString(), "totalAmount", totalAmount);
    }

    public List<Long> keyExtractor() {
        List<BookingListResponseBody> bookingListResponseBodies = bookingListService.findAll();
        bookingListResponseBodies.sort(new SortBookingListResponseBodyById());
        List<Long> keys = new ArrayList<>(bookingListResponseBodies.size());
        if (bookingListResponseBodies.size() > 0) {
            for (BookingListResponseBody body : bookingListResponseBodies) {
                keys.add(body.getId());
            }
        }
        return keys;
    }

    static class SortBookingListResponseBodyById implements Comparator<BookingListResponseBody> {
        @Override
        public int compare(BookingListResponseBody body, BookingListResponseBody body1) {
            return body.getId().compareTo(body1.getId());
        }
    }
}
