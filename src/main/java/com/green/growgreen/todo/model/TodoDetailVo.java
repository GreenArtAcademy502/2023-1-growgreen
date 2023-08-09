package com.green.growgreen.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoDetailVo {
    private int itodo;
    private int iplant;
    private String deadlineTime;
    private String deadlineDate;
    private String ctnt;
    private int repeatYn;
}
