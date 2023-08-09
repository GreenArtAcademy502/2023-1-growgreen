package com.green.growgreen.todo.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TodoAllDto {
    private TodoDetailVo todo;
    private List<String> repeatDay;
}
