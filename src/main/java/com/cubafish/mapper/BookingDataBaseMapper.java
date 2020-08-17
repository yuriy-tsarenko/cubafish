package com.cubafish.mapper;

import com.cubafish.dto.BookingDataBaseDto;
import com.cubafish.entity.BookingDataBase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingDataBaseMapper {
    List<BookingDataBaseDto> mapEntitiesToDtos(List<BookingDataBase> bookingDataBases);

    BookingDataBase mapDtoToEntity(BookingDataBaseDto bookingDataBaseDto);
}
