package com.green.growgreen.todo.model;

import lombok.Data;

@Data
public class TodoEntity {
    private int itodo;
    private int iplant;
    private String ctnt;
    private String deadlineTime;
    private String deadlineDate;
    private String createdAt;
    private String updatedAt;
    private int delYn;
    private int finishYn;
    private String finishedAt;
    private int repeatYn;
}
