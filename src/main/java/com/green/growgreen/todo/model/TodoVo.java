package com.green.growgreen.todo.model;


import lombok.Builder;
import lombok.Data;

@Data
public class TodoVo {
    private int itodo;
    private String ctnt;
    private String deadlineDate;
    private String deadlineTime;
    private String nickNm;
    private String nm;
    private int finishYn;
}
