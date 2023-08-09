package com.green.growgreen.diary;

import com.green.growgreen.diary.model.DiaryDelDto;
import com.green.growgreen.diary.model.DiaryEntity;
import com.green.growgreen.diary.model.DiarySelAllVo;
import com.green.growgreen.diary.model.DiarySelDetailDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiaryMapperTest {

    @Autowired
    private DiaryMapper mapper;

    @Test
    void insDiary() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowStr = now.format(formatter);

        //1차 테스트
        DiaryEntity entity = new DiaryEntity();
        entity.setTitle("타이틀1");
        entity.setCtnt("내용1");

        int result = mapper.insDiary(entity);
        assertEquals(1, result);
        assertNotEquals(0, entity.getIdiary());

        DiarySelDetailDto dto = new DiarySelDetailDto(entity.getIdiary());
        DiarySelAllVo vo = mapper.selDiaryById(dto);
        assertEquals(entity.getTitle(), vo.getTitle());
        assertEquals(entity.getCtnt(), vo.getCtnt());
        assertEquals(nowStr, vo.getCreatedAt());
        assertNull(vo.getPic());


        //2차 테스트
        DiaryEntity entity2 = new DiaryEntity();
        entity2.setTitle("타이틀2");
        entity2.setCtnt("내용2");

        int result2 = mapper.insDiary(entity2);
        assertEquals(1, result2);
        assertNotEquals(0, entity2.getIdiary());

        assertEquals(1, entity2.getIdiary() - entity.getIdiary());

        DiarySelDetailDto dto2 = new DiarySelDetailDto(entity2.getIdiary());
        DiarySelAllVo vo2 = mapper.selDiaryById(dto2);
        assertEquals(entity2.getTitle(), vo2.getTitle());
        assertEquals(entity2.getCtnt(), vo2.getCtnt());
        assertEquals(nowStr, vo2.getCreatedAt());
        assertNull(vo2.getPic());

    }

    @Test
    void delDiary() {
        DiaryDelDto dto = new DiaryDelDto();
        dto.setIdiary(1);

        int result = mapper.delDiary(dto);
        assertEquals(1, result);

        int result2 = mapper.delDiary(dto);
        assertEquals(0, result2);

        DiarySelDetailDto dto2 = new DiarySelDetailDto(1);
        DiarySelAllVo vo = mapper.selDiaryById(dto2);
        assertNull(vo);

        assertEquals(9, mapper.selDiaryAll().size());

        //-------
        DiaryDelDto dto3 = new DiaryDelDto();
        dto3.setIdiary(3);
        int result3 = mapper.delDiary(dto3);
        assertEquals(1, result3, "3번 레코드 삭제 안 됨");

        int result4 = mapper.delDiary(dto);
        assertEquals(0, result4);

        DiarySelDetailDto dto4 = new DiarySelDetailDto(3);
        DiarySelAllVo vo2 = mapper.selDiaryById(dto4);
        assertNull(vo2);

        assertEquals(8, mapper.selDiaryAll().size());
    }

    @Test
    void updDiary() {
        DiaryEntity entity = new DiaryEntity();
        entity.setIdiary(1);
        entity.setTitle("테스트1");
        entity.setCtnt("내용1");
        entity.setPic("사진.jpg");

        int result = mapper.updDiary(entity);
        assertEquals(1, result);

        DiarySelDetailDto dto = new DiarySelDetailDto(1);
        DiarySelAllVo vo = mapper.selDiaryById(dto);

        assertEquals(entity.getIdiary(), vo.getIdiary());
        assertEquals(entity.getTitle(), vo.getTitle());
        assertEquals(entity.getCtnt(), vo.getCtnt());
        assertEquals(entity.getPic(), vo.getPic());
        assertEquals("2023-01-15", vo.getCreatedAt());
    }

    @Test
    void selDiaryAll() {
        List<DiarySelAllVo> list = mapper.selDiaryAll();
        assertEquals(10, list.size());

        for(DiarySelAllVo vo : list) {
            DiarySelDetailDto dto = new DiarySelDetailDto(vo.getIdiary());
            DiarySelAllVo item = mapper.selDiaryById(dto);

            assertEquals(item.getIdiary(), vo.getIdiary());
            assertEquals(item.getTitle(), vo.getTitle());
            assertEquals(item.getCtnt(), vo.getCtnt());
            assertEquals(item.getCreatedAt(), vo.getCreatedAt());
            assertEquals(item.getPic(), vo.getPic());
        }
    }

    @Test
    void selDiaryById() {
        DiarySelDetailDto dto = new DiarySelDetailDto(1);
        DiarySelAllVo vo = mapper.selDiaryById(dto);
        assertEquals(1, vo.getIdiary());
        assertEquals("카스테라 잎이 갈라진 날", vo.getTitle());
        assertEquals("카스테라의 잎이 드디어 갈라지기 시작했다. 붙어있던 잎이 갈라지는게 넘나 신기!"
                , vo.getCtnt().trim());
        assertEquals("2023-01-15", vo.getCreatedAt());
        assertEquals("fc4e7756-6f7d-4e6e-8aac-d0b2e46f0c16.jpg", vo.getPic());

        DiarySelDetailDto dto2 = new DiarySelDetailDto(2);
        DiarySelAllVo vo2 = mapper.selDiaryById(dto2);
        assertEquals(2, vo2.getIdiary());
        assertEquals("향기가 굉장해 엄청나!", vo2.getTitle());
        assertEquals("데려온 날부터 쑥쑥 잘자라준 허브티. 혼자 잘 자라고 공기정화도 해주고..효자다 효자 장미향이 은은한게 너무 좋아~~~"
                , vo2.getCtnt().trim());
        assertEquals("2023-02-20", vo2.getCreatedAt());
        assertEquals("6458c54c-6fbb-4dfb-b848-7bc38e50accc.jpg", vo2.getPic());
    }
}