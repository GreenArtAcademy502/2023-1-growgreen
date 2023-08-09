package com.green.growgreen.diary;

import com.green.growgreen.diary.model.DiaryInsDto;
import com.green.growgreen.diary.model.DiarySelAllVo;
import com.green.growgreen.diary.model.DiarySelDetailDto;
import com.green.growgreen.utils.FileUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@Builder
class FileInfo {
    private String fileNm;
    private String ext;
    private String dic;

    public String getFullFileNm() {
        return String.format("%s.%s", fileNm, ext);
    }
    public String getPath() {
        return String.format("%s/%s", dic, getFullFileNm());
    }
    public FileInputStream getStream() throws Exception {
        return new FileInputStream(getPath());
    }
}

@SpringBootTest
class DiaryServiceTest {

    @Autowired
    private DiaryService service;

    @Autowired
    private DiaryMapper mapper;

    @Value("${file.dir}")
    private String uploadDic;

    @Test
    void allDto() throws Exception {
        DiaryInsDto dto = new DiaryInsDto();
        dto.setCtnt("테스트 중!");
        dto.setTitle("Test");

        FileInfo f1 = FileInfo.builder().fileNm("kara").ext("jpg").dic("D:/pics").build();
        FileInfo f2 = FileInfo.builder().fileNm("newjeans").ext("png").dic("D:/pics").build();
        FileInfo f3 = FileInfo.builder().fileNm("psx").ext("jpg").dic("D:/pics").build();
        FileInfo f4 = FileInfo.builder().fileNm("xbox").ext("jpg").dic("D:/pics").build();
        List<MultipartFile> imgList = new ArrayList();
        imgList.add(new MockMultipartFile("pics", f1.getFullFileNm(), f1.getExt(), f1.getStream()));
        imgList.add(new MockMultipartFile("pics", f2.getFullFileNm(), f2.getExt(), f2.getStream()));
        imgList.add(new MockMultipartFile("pics", f3.getFullFileNm(), f3.getExt(), f3.getStream()));
        imgList.add(new MockMultipartFile("pics", f4.getFullFileNm(), f4.getExt(), f4.getStream()));

        int iDiary = service.allDto(dto, imgList);
        assertNotEquals(0, iDiary);

        DiarySelDetailDto p1 = new DiarySelDetailDto(iDiary);
        List<String> dbImgList = mapper.selDiaryDetailPics(p1);

        String absoluteUploadPath = FileUtils.getAbsoluteDownloadPath(uploadDic) + "/diaryPics/" + iDiary;
        int idx = 1;
        for(String img : dbImgList) {
            File file = new File(absoluteUploadPath, img);
            assertEquals(true, file.exists(), String.format("%d번째 이미지가 없습니다.", idx++));
        }
        DiarySelAllVo dbDiary = mapper.selDiaryById(p1);
        assertEquals(dbDiary.getPic(), dbImgList.get(0), "썸네일 파일명이 다릅니다.");
    }

    @Test
    void putDiaryPics() { }

    @Test
    void updDiary() { }
}