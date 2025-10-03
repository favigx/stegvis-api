package com.stegvis_api.stegvis_api.todo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.stegvis_api.stegvis_api.todo.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.DeleteTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.TodoResponse;
import com.stegvis_api.stegvis_api.todo.model.Todo;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "dateTimeCreated", ignore = true)
    Todo toTodo(AddTodoDTO dto);

    AddTodoResponse toAddTodoResponse(Todo todo);

    List<TodoResponse> toTodoResponseList(List<Todo> todos);

    @Mapping(target = "deletedAt", expression = "java(java.time.Instant.now().toString())")
    @Mapping(target = "message", constant = "Todo har raderats framgångsrikt")
    DeleteTodoResponse toDeleteTodoResponse(Todo todo);

}