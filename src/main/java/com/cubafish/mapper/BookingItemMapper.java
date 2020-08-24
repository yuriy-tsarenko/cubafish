package com.cubafish.mapper;

import com.cubafish.dto.BookingItemDto;
import com.cubafish.entity.BookingItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingItemMapper {
    List<BookingItemDto> mapEntitiesToDtos(List<BookingItem> bookingItems);

    BookingItem mapDtoToEntity(BookingItemDto bookingItemDto);

    BookingItemDto mapEntityToDto(BookingItem bookingItem);
}
