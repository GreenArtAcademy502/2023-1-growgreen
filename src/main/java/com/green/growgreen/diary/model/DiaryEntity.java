package com.green.growgreen.diary.model;

import lombok.Data;

@Data
public class DiaryEntity {
    private int idiary;
    private String title;
    private String ctnt;
    private String createdAt;
    private String pic;
}
