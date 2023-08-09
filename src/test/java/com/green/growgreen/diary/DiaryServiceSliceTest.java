package com.green.growgreen.diary;

import com.green.growgreen.diary.model.DiaryAllDto;
import com.green.growgreen.diary.model.DiarySelAllVo;
import com.green.growgreen.diary.model.DiarySelDetailDto;
import com.green.growgreen.todo.TodoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({DiaryService.class})
public class DiaryServiceSliceTest {
    @MockBean
    private DiaryMapper mapper;

    @Autowired
    private DiaryService service;

    @Test
    void selDiary() {
        List<DiarySelAllVo> expectedList = new ArrayList();
        expectedList.add(new DiarySelAllVo(1, "테스트1", "내용1", "2023-08-09", "a1.jpg"));
        expectedList.add(new DiarySelAllVo(2, "테스트2", "내용2", "2023-08-08", "a2.jpg"));
        expectedList.add(new DiarySelAllVo(3, "테스트3", "내용3", "2023-08-07", "a3.jpg"));

        when(mapper.selDiaryAll()).thenReturn(expectedList);

        List<DiarySelAllVo> actualList = service.selDiary();
        assertEquals(expectedList.size(), actualList.size());
        for(int i=0; i<expectedList.size(); i++) {
            DiarySelAllVo ex = expectedList.get(i);
            DiarySelAllVo al = actualList.get(i);

            assertEquals(ex.getIdiary(), al.getIdiary(), String.format("%d번 - idiary 상이", i));
            assertEquals(ex.getTitle(), al.getTitle(), String.format("%d번 - 제목 상이", i));
            assertEquals(ex.getCtnt(), al.getCtnt(), String.format("%d번 - 내용 상이", i));
            assertEquals(ex.getCreatedAt(), al.getCreatedAt(), String.format("%d번 - getCreatedAt 상이", i));
            assertEquals(ex.getPic(), al.getPic(), String.format("%d번 - pic 상이", i));
        }
        verify(mapper).selDiaryAll();
    }

    @Test
    void selById() {
        //DiarySelDetailDto p = new DiarySelDetailDto(1);

        DiarySelAllVo expectedVo = new DiarySelAllVo(1, "테스트1", "내용1", "2023-08-09", "a1.jpg");
        when(mapper.selDiaryById(any())).thenReturn(expectedVo);

        DiarySelAllVo actualVo = service.selById(any());

        assertNotNull(actualVo);
        assertEquals(expectedVo.getIdiary(), actualVo.getIdiary());
        assertEquals(expectedVo.getTitle(), actualVo.getTitle());
        assertEquals(expectedVo.getCtnt(), actualVo.getCtnt());
        assertEquals(expectedVo.getCreatedAt(), actualVo.getCreatedAt());
        assertEquals(expectedVo.getPic(), actualVo.getPic());

        verify(mapper).selDiaryById(any());
    }

    @Test
    void selDiaryPicData() {
        List<String> pics = new ArrayList();
        pics.add("a1.jpg");
        pics.add("a2.jpg");

        DiarySelAllVo vo = new DiarySelAllVo(1, "테스트1", "내용1", "2023-08-09", "a1.jpg");

        DiaryAllDto expectedDto = DiaryAllDto.builder()
                                            .data(vo)
                                            .pics(pics)
                                            .build();

        when(mapper.selDiaryById(any())).thenReturn(vo);
        when(mapper.selDiaryDetailPics(any())).thenReturn(pics);

        DiaryAllDto actualDto = service.selDiaryPicData(any());
        assertNotNull(actualDto);

        assertEquals(expectedDto.getData().getIdiary(), actualDto.getData().getIdiary());
        assertEquals(expectedDto.getData().getTitle(), actualDto.getData().getTitle());
        assertEquals(expectedDto.getData().getCtnt(), actualDto.getData().getCtnt());
        assertEquals(expectedDto.getData().getCreatedAt(), actualDto.getData().getCreatedAt());
        assertEquals(expectedDto.getData().getPic(), actualDto.getData().getPic());

        assertEquals(pics.size(), actualDto.getPics().size());

        for(int i=0; i<pics.size(); i++) {
            assertEquals(pics.get(i), actualDto.getPics().get(i));
        }

        verify(mapper).selDiaryById(any());
        verify(mapper).selDiaryDetailPics(any());
    }

    @Test
    void delDiary() {
    }

}
