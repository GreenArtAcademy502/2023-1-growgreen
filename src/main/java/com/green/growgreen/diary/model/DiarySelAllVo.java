package com.green.growgreen.diary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiarySelAllVo {
    private int idiary;
    private String title;
    private String ctnt;
    private String createdAt;
    private String pic;
}
