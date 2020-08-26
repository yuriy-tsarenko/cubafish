package com.cubafish.service;

import com.cubafish.dto.BookingDataBaseDto;
import com.cubafish.dto.BookingItemDto;
import com.cubafish.dto.BookingListDto;
import com.cubafish.dto.ProductDto;
import com.cubafish.dto.StatisticsDto;
import com.cubafish.entity.BookingList;
import com.cubafish.entity.Product;
import com.cubafish.entity.Statistics;
import com.cubafish.mapper.BookingDataBaseMapper;
import com.cubafish.mapper.BookingListMapper;
import com.cubafish.mapper.ProductMapper;
import com.cubafish.mapper.StatisticsMapper;
import com.cubafish.repository.BookingDataBaseRepository;
import com.cubafish.repository.BookingListRepository;
import com.cubafish.repository.ProductRepository;
import com.cubafish.repository.StatisticsRepository;
import com.cubafish.utils.BookingListResponseBody;
import com.cubafish.utils.CustomRequestBody;

import com.cubafish.utils.CustomResponseBody;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Data
public class BookingDataBaseService {

    @Value("${product.currency}")
    private String currency;

    @Value("${product.value-type}")
    private String valueType;

    private final BookingDataBaseRepository bookingDataBaseRepository;
    private final BookingDataBaseMapper bookingDataBaseMapper;
    private final BookingListMapper bookingListMapper;
    private final BookingListRepository bookingListRepository;
    private final BookingItemService bookingItemService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final StatisticsRepository statisticsRepository;
    private final StatisticsService statisticsService;
    private final StatisticsMapper statisticsMapper;

    public List<BookingDataBaseDto> findAll() {
        List<BookingDataBaseDto> bookingDataBaseDtos = bookingDataBaseMapper
                .mapEntitiesToDtos(bookingDataBaseRepository.findAll());
        bookingDataBaseDtos.sort(new SortBookingDataBaseById());
        return bookingDataBaseDtos;
    }

    public CustomResponseBody approveOrCancelBookingOperation(CustomRequestBody customRequestBody) {
        String status = "null";
        int step = 0;
        StringBuilder builderForItems = new StringBuilder();
        List<BookingListDto> bookingListDtos = new ArrayList<>(1);
        BookingListDto bookingListDto = bookingListMapper
                .mapEntityToDto(bookingListRepository.findBookingListById(Long.valueOf(customRequestBody.getKey())));
        bookingListDtos.add(bookingListDto);
        List<BookingListResponseBody> bookingListResponseBody =
                bookingItemService.mapBookingListDtosToBookingListResponseBodies(bookingListDtos);
        if (bookingListResponseBody.size() != 0) {
            for (BookingListResponseBody item : bookingListResponseBody) {
                List<BookingItemDto> bookingItemDtos = item.getBookingItems();
                for (BookingItemDto bookingItemDto : bookingItemDtos) {
                    ProductDto productDto =
                            productMapper.mapEntityToDto(productRepository
                                    .getOne(Long.valueOf(bookingItemDto.getKey())));
                    step++;
                    if (productDto != null) {
                        Product product = productRepository.getOne(productDto.getId());
                        productDto.setTotalAmount(productDto.getTotalAmount() - bookingItemDto.getItemAmount());
                        BeanUtils.copyProperties(productDto, product);
                        productRepository.save(product);
                        builderForItems.append(bookingItemDto.getDescription());
                        builderForItems.append(" - ");
                        builderForItems.append(bookingItemDto.getItemAmount());
                        builderForItems.append(valueType);
                        builderForItems.append(" - ");
                        builderForItems.append(bookingItemDto.getProductPrice());
                        builderForItems.append(currency);
                        builderForItems.append(" *** ");
                        status = "success";
                        step++;
                    }
                }
                if (status.equals("success")) {
                    BookingDataBaseDto bookingDataBaseDto =
                            mapBookingListResponseBodyToBookingDataBaseDto(item, builderForItems.toString(),
                                    customRequestBody);
                    if (bookingDataBaseDto != null) {
                        bookingDataBaseRepository.save(bookingDataBaseMapper.mapDtoToEntity(bookingDataBaseDto));
                        status = "success";
                    }
                    step++;
                }
                if (status.equals("success")) {

                    Map<String, Object> responseFromUpdateOrSaveStatistics =
                            statisticsService.updateOrSaveStatistics(item, customRequestBody);
                    boolean flag = (boolean) responseFromUpdateOrSaveStatistics.get("flag");
                    if (flag) {
                        Statistics statistics
                                = (Statistics) responseFromUpdateOrSaveStatistics.get("statisticsFromDb");
                        statisticsRepository.save(statistics);
                    } else {
                        StatisticsDto statisticsDto
                                = (StatisticsDto) responseFromUpdateOrSaveStatistics.get("statisticsDto");
                        statisticsRepository.save(statisticsMapper.mapDtoToEntity(statisticsDto));
                    }
                    BookingList bookingList =
                            bookingListRepository.findBookingListById(Long.valueOf(customRequestBody.getKey()));
                    bookingListRepository.delete(bookingList);
                    step++;
                }
            }
        } else {
            status = "can not map booking list dto to booking list response body";
        }

        return new CustomResponseBody(1L, "approve status", status, "no data",
                "approving step", step);
    }

    public BookingDataBaseDto mapBookingListResponseBodyToBookingDataBaseDto(
            BookingListResponseBody bookingListResponseBody, String bookingItemsLine,
            CustomRequestBody customRequestBody) {
        BookingDataBaseDto bookingDataBaseDto = new BookingDataBaseDto();
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bookingDataBaseDto.setDateOfBooking(bookingListResponseBody.getDateOfBooking());
        bookingDataBaseDto.setDateOfSavingToDb(dateFormat.format(currentDate));
        bookingDataBaseDto.setFirstName(bookingListResponseBody.getFirstName());
        bookingDataBaseDto.setMiddleName(bookingListResponseBody.getMiddleName());
        bookingDataBaseDto.setLastName(bookingListResponseBody.getLastName());
        bookingDataBaseDto.setEmail(bookingListResponseBody.getEmail());
        bookingDataBaseDto.setContact(bookingListResponseBody.getContact());
        if (customRequestBody.getCommunicationKey().equals("cancel booking")) {
            bookingDataBaseDto.setUserConfirmation("Заказ отменен");
        } else {
            bookingDataBaseDto.setUserConfirmation(bookingListResponseBody.getUserConfirmation());
        }
        bookingDataBaseDto.setTotalPrice(bookingListResponseBody.getTotalPrice());
        bookingDataBaseDto.setTotalAmount(bookingListResponseBody.getTotalAmount());
        bookingDataBaseDto.setPaymentType(bookingListResponseBody.getPaymentType());
        bookingDataBaseDto.setDeliveryType(bookingListResponseBody.getDeliveryType());
        bookingDataBaseDto.setRegion(bookingListResponseBody.getRegion());
        bookingDataBaseDto.setCity(bookingListResponseBody.getCity());
        bookingDataBaseDto.setAddress(bookingListResponseBody.getAddress());
        bookingDataBaseDto.setBookingItems(bookingItemsLine);
        return bookingDataBaseDto;
    }

    static class SortBookingDataBaseById implements Comparator<BookingDataBaseDto> {
        @Override
        public int compare(BookingDataBaseDto bookingDataBaseDto, BookingDataBaseDto bookingDataBaseDto1) {
            return bookingDataBaseDto.getId().compareTo(bookingDataBaseDto1.getId());
        }
    }
}
