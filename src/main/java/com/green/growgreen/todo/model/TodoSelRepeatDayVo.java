package com.green.growgreen.todo.model;

import lombok.Data;

@Data
public class TodoSelRepeatDayVo {
    private String ctnt;
    private String deadlineTime;
    private String deadlineDate;
    private String nickNm;
    private String nm;
    private int repeatDay;
}