package com.github.devraghav.springexamples.todo.mapper;

import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import com.github.devraghav.springexamples.todo.dto.response.TodoResponse;
import com.github.devraghav.springexamples.todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TodoMapper {

  TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

  @Mappings({
    @Mapping(target = "id", expression = MappingUtils.GENERATE_UUID_EXPRESSION),
    @Mapping(target = "sectionId", source = "sectionId")
  })
  Todo requestToEntity(String sectionId, TodoRequest request);

  TodoResponse entityToResponse(Todo todo);
}
