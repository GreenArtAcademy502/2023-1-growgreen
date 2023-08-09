package com.green.growgreen.todo.model;

import lombok.Data;

import java.util.List;

@Data
public class TodoInsDto {
    private int iplant;
    private String ctnt;
    private String deadlineTime;
    private String deadlineDate;
    private int repeatYn;
    private List<Integer> repeatDay;
}
