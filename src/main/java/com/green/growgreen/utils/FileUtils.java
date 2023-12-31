package com.green.growgreen.utils;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

public class FileUtils {
    // 파일의 확장자 리턴하는 메소드
    public static String getExt(String fileNm) {
        int dotIdx = fileNm.lastIndexOf(".");
        String ext = fileNm.substring(dotIdx);
        return ext;
    }

    public static String getFile (String fileNm) {
        int dotIdx = fileNm.lastIndexOf(".");
        String ext = fileNm.substring(0, dotIdx);
        return ext;
    }
    public static String makeRandomFileNm (String fileNm) {
        String uuid = UUID.randomUUID().toString();
        String saveNm = uuid + getExt(fileNm);
        return saveNm;
    }
    public static int getDate () {
        // LocalDate 생성
        LocalDate date = LocalDate.now();
        // DayOfWeek 객체 구하기
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        // 숫자요일 구하기
        int dayOfWeekNumber = dayOfWeek.getValue();
        return dayOfWeekNumber - 1;
    }

    public static String getAbsoluteDownloadPath(String path) {
        File file = new File(path);
        return file.getAbsolutePath();
    }
}
