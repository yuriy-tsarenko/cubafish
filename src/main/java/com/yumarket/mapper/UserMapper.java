package com.yumarket.mapper;

import com.yumarket.dto.UserDto;
import com.yumarket.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserDto> mapEntitiesToDtos(List<User> users);

    UserDto mapEntityToDto(User user);

    User mapDtoToEntity(UserDto userDto);
}
