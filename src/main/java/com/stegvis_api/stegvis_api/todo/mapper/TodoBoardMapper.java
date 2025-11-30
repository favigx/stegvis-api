package com.stegvis_api.stegvis_api.todo.mapper;

import java.time.Instant;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.stegvis_api.stegvis_api.todo.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.DeleteTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.TodoResponse;
import com.stegvis_api.stegvis_api.todo.enums.TodoStatus;
import com.stegvis_api.stegvis_api.todo.model.TodoBoard;

@Mapper(componentModel = "spring")
public interface TodoBoardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "dateTimeCreated", ignore = true)
    @Mapping(target = "dateTimeCompleted", ignore = true)
    @Mapping(target = "status", ignore = true)
    TodoBoard toTodo(AddTodoDTO dto);

    @AfterMapping
    default void setDefaults(@MappingTarget TodoBoard todo) {
        if (todo.getStatus() == null) {
            todo.setStatus(TodoStatus.TODO);
        }
        if (todo.getDateTimeCreated() == null) {
            todo.setDateTimeCreated(Instant.now());
        }
    }

    AddTodoResponse toAddTodoResponse(TodoBoard todo);

    List<TodoResponse> toTodoResponseList(List<TodoBoard> todos);

    @Mapping(target = "deletedAt", expression = "java(java.time.Instant.now().toString())")
    @Mapping(target = "message", constant = "Todo har raderats framgångsrikt")
    DeleteTodoResponse toDeleteTodoResponse(TodoBoard todo);

}