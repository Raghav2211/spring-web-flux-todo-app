package com.github.devraghav.springexamples.todo.mapper;

import com.github.devraghav.springexamples.todo.dto.request.SectionRequest;
import com.github.devraghav.springexamples.todo.dto.response.SectionResponse;
import com.github.devraghav.springexamples.todo.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SectionMapper {
  SectionMapper INSTANCE = Mappers.getMapper(SectionMapper.class);

  @Mappings({
    @Mapping(target = "id", expression = MappingUtils.GENERATE_UUID_EXPRESSION),
    @Mapping(target = "userId", source = "userId")
  })
  Section requestToEntity(String userId, SectionRequest request);

  SectionResponse entityToResponse(Section section);
}
