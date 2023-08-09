package com.green.growgreen.diary.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DiaryAllDto {
    private DiarySelAllVo data;
    private List<String> pics;
}
