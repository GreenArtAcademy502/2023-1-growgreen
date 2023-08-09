package com.green.growgreen.diary;

import com.green.growgreen.diary.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper {

    // 기본 rest full
    int insDiary (DiaryEntity entity); // 이미지 포함 insert
    int delDiary (DiaryDelDto dto); // delete
    int updDiary (DiaryEntity entity); // update
    int updDiary1 (DiaryEntity entity);

    // select 문
    List<DiarySelAllVo> selDiaryAll (); // 전체 select
    DiarySelAllVo selDiaryById (DiarySelDetailDto dto); // 디테일한 다이어리 select
    List<String> selDiaryDetailPics(DiarySelDetailDto dto); // 디테일 클릭시 보이는 여러 이미지 SELECT

    // 다중파일 업로드 insert, delete
    int insDiaryPic (List<DiaryPicEntity> entity); // 다중이미지 업로드
    int delDiarySubPic (DiaryDelSubPicsDto dto); // 다중이미지 삭제
    int updDiaryMainPic (DiaryUpdDiaryMainPicDto dto);

}
