package com.cubafish.service;

import com.cubafish.dto.BookingItemDto;
import com.cubafish.dto.BookingListDto;
import com.cubafish.entity.BookingItem;
import com.cubafish.mapper.BookingItemMapper;
import com.cubafish.repository.BookingItemRepository;
import com.cubafish.utils.BookingListResponseBody;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Data
public class BookingItemService {

    private final BookingItemRepository bookingItemRepository;
    private final BookingItemMapper bookingItemMapper;

    public Map<String, Object> findById(Long id) {
        BookingItem bookingItem = bookingItemRepository.findBookingItemById(id);
        return Map.of("bookingItem", bookingItem, "status", "success");
    }

    public BookingItem create(BookingItemDto bookingItemDto) {
        return bookingItemMapper.mapDtoToEntity(bookingItemDto);
    }

    public Map<String, Object> saveItemsFromList(List<BookingItemDto> bookingItems) {
        StringBuilder itemsIdBuilder = new StringBuilder();
        Date currentDate = new Date();
        DateFormat dateFormatWithMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ms");
        for (int i = 0; i < bookingItems.size(); i++) {
            String uuid = UUID.randomUUID().toString();
            bookingItems.get(i).setCreationTime(dateFormatWithMs.format(currentDate).concat("-uuid-").concat(uuid));
            if (bookingItems.get(i).getCreationTime() != null) {
                bookingItemRepository.save(create(bookingItems.get(i)));
                BookingItemDto bookingItemDto = bookingItemMapper
                        .mapEntityToDto(bookingItemRepository
                                .findBookingItemByCreationTime(bookingItems.get(i).getCreationTime()));
                itemsIdBuilder.append(bookingItemDto.getId());
                itemsIdBuilder.append("-");
            } else {
                return Map.of("result", itemsIdBuilder.toString(), "status", "can not get creation time");
            }
        }
        return Map.of("result", itemsIdBuilder.toString(), "status", "success");
    }

    public List<BookingItemDto> addItemsToList(String bookingItemStringFromDb) {
        String[] items = bookingItemStringFromDb.split("-");
        List<BookingItemDto> bookingItems = new ArrayList<>(items.length);
        for (String item : items) {
            Map<String, Object> responseFromFindById = findById(Long.valueOf(item.trim()));
            BookingItem bookingItem = (BookingItem) responseFromFindById.get("bookingItem");
            bookingItems.add(bookingItemMapper.mapEntityToDto(bookingItem));
        }
        return bookingItems;
    }

    public List<BookingListResponseBody> mapBookingListDtosToBookingListResponseBodies(
            List<BookingListDto> bookingListDtos) {
        List<BookingListResponseBody> bookingListResponseBodies = new ArrayList<>(bookingListDtos.size());
        for (BookingListDto dto : bookingListDtos) {
            BookingListResponseBody responseBody = new BookingListResponseBody();
            responseBody.setId(dto.getId());
            responseBody.setDateOfBooking(dto.getDateOfBooking());
            responseBody.setFirstName(dto.getFirstName());
            responseBody.setMiddleName(dto.getMiddleName());
            responseBody.setLastName(dto.getLastName());
            responseBody.setEmail(dto.getEmail());
            responseBody.setContact(dto.getContact());
            responseBody.setUserConfirmation(dto.getUserConfirmation());
            responseBody.setTotalPrice(dto.getTotalPrice());
            responseBody.setTotalAmount(dto.getTotalAmount());
            responseBody.setPaymentType(dto.getPaymentType());
            responseBody.setDeliveryType(dto.getDeliveryType());
            responseBody.setRegion(dto.getRegion());
            responseBody.setCity(dto.getCity());
            responseBody.setAddress(dto.getAddress());
            responseBody.setBookingComments(dto.getBookingComments());
            List<BookingItemDto> bookingDtoItems = new ArrayList<>(addItemsToList(dto.getBookingItems()));
            responseBody.setBookingItems(bookingDtoItems);
            bookingListResponseBodies.add(responseBody);
        }
        return bookingListResponseBodies;
    }

    public Map<String, Object> updateExitingItemsFromList(String bookingItemStringFromDb,
                                                          List<BookingItemDto> bookingItemDtos) {
        StringBuilder itemsIdBuilder = new StringBuilder();
        Date currentDate = new Date();
        DateFormat dateFormatWithMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ms");

        String[] items = bookingItemStringFromDb.split("-");
        List<BookingItem> bookingItems = new ArrayList<>(items.length);
        List<BookingItem> newBookingItems = new ArrayList<>(bookingItemDtos.size());
        for (String item : items) {
            Map<String, Object> responseFromFindById = findById(Long.valueOf(item.trim()));
            BookingItem bookingItem = (BookingItem) responseFromFindById.get("bookingItem");
            bookingItems.add(bookingItem);
        }
        for (BookingItemDto bookingItemDto : bookingItemDtos) {
            newBookingItems.add(bookingItemMapper.mapDtoToEntity(bookingItemDto));
        }
        bookingItems.sort(new SortBookingItemByKey());
        newBookingItems.sort(new SortBookingItemByKey());

        if (items.length < bookingItemDtos.size()) {
            int lastIndex = 0;
            for (int i = 0; i < bookingItems.size(); i++) {
                for (int z = 0; z < 1; z++) {

                    String uuid = UUID.randomUUID().toString();
                    newBookingItems.get(i).setCreationTime(dateFormatWithMs
                            .format(currentDate).concat("-uuid-").concat(uuid));

                    BeanUtils.copyProperties(newBookingItems.get(i), bookingItems.get(i), "id");
                    bookingItemRepository.save(bookingItems.get(i));
                    BookingItemDto bookingItemDto =
                            bookingItemMapper.mapEntityToDto(bookingItemRepository
                                    .findBookingItemByCreationTime(bookingItems.get(i).getCreationTime()));
                    itemsIdBuilder.append(bookingItemDto.getId());
                    itemsIdBuilder.append("-");
                    lastIndex++;
                }
            }
            for (; lastIndex < newBookingItems.size(); lastIndex++) {
                String uuid = UUID.randomUUID().toString();
                newBookingItems.get(lastIndex).setCreationTime(dateFormatWithMs
                        .format(currentDate).concat("-uuid-").concat(uuid));
                bookingItemRepository.save(newBookingItems.get(lastIndex));
                BookingItemDto bookingItemDto =
                        bookingItemMapper.mapEntityToDto(bookingItemRepository
                                .findBookingItemByCreationTime(newBookingItems.get(lastIndex).getCreationTime()));
                itemsIdBuilder.append(bookingItemDto.getId());
                itemsIdBuilder.append("-");
            }
        }

        if (items.length == bookingItemDtos.size()) {
            for (int i = 0; i < bookingItems.size(); i++) {
                for (int z = 0; z < 1; z++) {
                    String uuid = UUID.randomUUID().toString();
                    newBookingItems.get(i).setCreationTime(dateFormatWithMs
                            .format(currentDate).concat("-uuid-").concat(uuid));
                    BeanUtils.copyProperties(newBookingItems.get(i), bookingItems.get(i), "id");
                    bookingItemRepository.save(bookingItems.get(i));
                    BookingItemDto bookingItemDto =
                            bookingItemMapper.mapEntityToDto(bookingItemRepository
                                    .findBookingItemByCreationTime(bookingItems.get(i).getCreationTime()));
                    itemsIdBuilder.append(bookingItemDto.getId());
                    itemsIdBuilder.append("-");
                }
            }
        }

        if (items.length > bookingItemDtos.size()) {
            int lastIndex = 0;
            for (int i = 0; i < bookingItemDtos.size(); i++) {
                for (int z = 0; z < 1; z++) {
                    String uuid = UUID.randomUUID().toString();
                    newBookingItems.get(i).setCreationTime(dateFormatWithMs
                            .format(currentDate).concat("-uuid-").concat(uuid));
                    BeanUtils.copyProperties(newBookingItems.get(i), bookingItems.get(i), "id");
                    bookingItemRepository.save(bookingItems.get(i));
                    BookingItemDto bookingItemDto =
                            bookingItemMapper.mapEntityToDto(bookingItemRepository
                                    .findBookingItemByCreationTime(newBookingItems.get(i).getCreationTime()));
                    itemsIdBuilder.append(bookingItemDto.getId());
                    itemsIdBuilder.append("-");
                    lastIndex++;
                }
            }
            for (; lastIndex < bookingItems.size(); lastIndex++) {
                bookingItemRepository.delete(bookingItems.get(lastIndex));
            }
        }
        return Map.of("status", "success", "result", itemsIdBuilder.toString());
    }

    static class SortBookingItemByKey implements Comparator<BookingItem> {
        @Override
        public int compare(BookingItem bookingItem, BookingItem bookingItem2) {
            return bookingItem.getKey().compareTo(bookingItem2.getKey());
        }
    }

}
