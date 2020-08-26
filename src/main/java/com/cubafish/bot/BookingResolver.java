package com.cubafish.bot;

import com.cubafish.dto.BookingItemDto;
import com.cubafish.service.BookingListService;
import com.cubafish.utils.BookingListResponseBody;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Data
public class BookingResolver {
    private final BookingListService bookingListService;

    public Map<String, Object> getMessageForAdmin() {
        StringBuilder messageBuilder = new StringBuilder();
        List<BookingListResponseBody> bookingListResponseBodies = bookingListService.findAll();
        int totalAmount = bookingListResponseBodies.size();
        int id = 1;
        if (totalAmount > 0) {
            BookingListResponseBody bookingListResponseBody = bookingListResponseBodies.get(totalAmount - 1);
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
}
