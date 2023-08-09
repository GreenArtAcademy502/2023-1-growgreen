package com.green.growgreen.todo.model;

import lombok.Data;
import java.util.List;

@Data
public class TodoUpdDto {
    private int itodo;
    private int iplant;
    private String deadlineTime;
    private String deadlineDate;
    private String ctnt;
    private int repeatYn;
    private List<Integer> repeatDay;

}
