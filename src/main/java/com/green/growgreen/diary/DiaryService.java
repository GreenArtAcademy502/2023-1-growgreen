package com.green.growgreen.diary;

import com.green.growgreen.diary.model.*;
import com.green.growgreen.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryMapper MAPPER;

    @Value("${file.dir}")
    private String fileDir;

    public int allDto (DiaryInsDto dto, List<MultipartFile> pics) throws Exception{
        int result = 0;

        DiaryEntity entity = new DiaryEntity();
        entity.setCtnt(dto.getCtnt());
        entity.setTitle(dto.getTitle());

       result = MAPPER.insDiary(entity);

        if (pics != null) {
            String centerPath = String.format("diaryPics/%d", entity.getIdiary());
            String targetPath = String.format("%s/%s", FileUtils.getAbsoluteDownloadPath(fileDir), centerPath);

            File file = new File(targetPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            List<DiaryPicEntity> list = new ArrayList<>();

            for (int i = 0; i < pics.size(); i++) {
                String originFile = pics.get(i).getOriginalFilename();
                String saveName = FileUtils.makeRandomFileNm(originFile);

                File fileTarget = new File(targetPath+ "/" + saveName);

                try {
                    pics.get(i).transferTo(fileTarget);
                } catch (IOException e) {
                    throw new Exception("파일저장을 실패했습니다.");
                }

                DiaryPicEntity picEntity = new DiaryPicEntity();
                picEntity.setIdiary(entity.getIdiary());
                picEntity.setPic(saveName);
                list.add(picEntity);
            }
            DiaryUpdDiaryMainPicDto picDto = new DiaryUpdDiaryMainPicDto();
            picDto.setPic(list.get(0).getPic());
            picDto.setIdiary(entity.getIdiary());
            MAPPER.updDiaryMainPic(picDto);
            result = MAPPER.insDiaryPic(list);
        }
        //return result;
        return entity.getIdiary();
    }

    // 다이어리 다중 파일 삭제
    public int putDiaryPics (DiaryDelSubPicsDto dto)   {
        return MAPPER.delDiarySubPic(dto);
    }

    // 썸네일과 내용 수정하기
    public int updDiary (MultipartFile pic, DiaryUpdDto dto){

        DiaryEntity entity = new DiaryEntity();
        entity.setIdiary(dto.getIdiary());
        entity.setCtnt(dto.getCtnt());
        entity.setTitle(dto.getTitle());
        //pic이 null일 때
        MAPPER.updDiary1(entity);

        if(pic != null) {
            String saveFileName = FileUtils.makeRandomFileNm(pic.getOriginalFilename());
            entity.setPic(saveFileName);

            int result = MAPPER.updDiary(entity);

            String centPath = String.format("diaryPics/%d", dto.getIdiary());
            String targetDir = String.format("%s/%s", FileUtils.getAbsoluteDownloadPath(fileDir), centPath);

            File file = new File(targetDir);
            if (!file.exists()){
                file.mkdirs();
            }

            File fileTarget = new File(targetDir + "/" + saveFileName);

            try {
                pic.transferTo(fileTarget);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 1;
    }

    // 전체 리스트 보기
    public List<DiarySelAllVo> selDiary(){
        return MAPPER.selDiaryAll();
    }

    // 하나의 디테일 다이어리 보기
    public DiarySelAllVo selById (DiarySelDetailDto dto) {
        return MAPPER.selDiaryById(dto);
    }

    // 하나의 디테일한 리스트와 그 하위 이미지들 보기
    public DiaryAllDto selDiaryPicData (DiarySelDetailDto dto) {

        DiarySelAllVo data = MAPPER.selDiaryById(dto);
        List<String> pics = MAPPER.selDiaryDetailPics(dto);
        return DiaryAllDto
                .builder()
                .data(data)
                .pics(pics)
                .build();
    }

    // 다이어리 삭제 다중이미지도 함께 삭제
    public int delDiary (DiaryDelDto dto) {
        return MAPPER.delDiary(dto);
    }
}
