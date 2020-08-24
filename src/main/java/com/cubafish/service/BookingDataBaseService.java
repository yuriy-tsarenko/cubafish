package com.cubafish.service;

import com.cubafish.dto.BookingDataBaseDto;
import com.cubafish.dto.BookingItemDto;
import com.cubafish.mapper.BookingDataBaseMapper;
import com.cubafish.repository.BookingDataBaseRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Data
public class BookingDataBaseService {

    private final BookingDataBaseRepository bookingDataBaseRepository;
    private final BookingDataBaseMapper bookingDataBaseMapper;

    public List<BookingDataBaseDto> findAll() {
        List<BookingDataBaseDto> bookingDataBaseDtos = bookingDataBaseMapper
                .mapEntitiesToDtos(bookingDataBaseRepository.findAll());
        bookingDataBaseDtos.sort(new SortBookingDataBaseById());
        return bookingDataBaseDtos;
    }

    static class SortBookingDataBaseById implements Comparator<BookingDataBaseDto> {
        @Override
        public int compare(BookingDataBaseDto bookingDataBaseDto, BookingDataBaseDto bookingDataBaseDto1) {
            return bookingDataBaseDto.getId().compareTo(bookingDataBaseDto1.getId());
        }
    }
}
