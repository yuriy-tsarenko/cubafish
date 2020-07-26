package com.cubafish.mapper;

import com.cubafish.dto.UserDto;
import com.cubafish.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserDto> mapEntitiesToDtos(List<User> users);

    UserDto mapEntityToDto(User user);

    User mapDtoToEntity(UserDto userDto);
}
