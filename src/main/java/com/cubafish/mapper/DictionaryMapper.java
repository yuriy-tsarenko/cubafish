package com.cubafish.mapper;

import com.cubafish.dto.DictionaryDto;
import com.cubafish.entity.Dictionary;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {
    List<DictionaryDto> mapEntitiesToDtos(List<Dictionary> dictionaries);
}
