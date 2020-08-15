package com.cubafish.mapper;

import com.cubafish.dto.BookingListDto;
import com.cubafish.entity.BookingList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingListMapper {
    List<BookingListDto> mapEntitiesToDtos(List<BookingList> bookingLists);

    BookingList mapDtoToEntity(BookingListDto bookingListDto);
}
