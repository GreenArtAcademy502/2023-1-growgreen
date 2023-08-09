package com.green.growgreen.plant;


import com.green.growgreen.plant.model.*;
import com.green.growgreen.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantService {
    @Value("${file.dir}")
    private String fileDir;

    private final PlantMapper MAPPER;

    public int insPlant(MultipartFile img, PlantInsDto dto) {

        int result = 0;
        if(img != null){
            PlantEntity entity = new PlantEntity();
            entity.setNm(dto.getNm());
            entity.setNickNm(dto.getNickNm());
            entity.setOnDate(dto.getOnDate());
            entity.setCtnt(dto.getCtnt());

            // 랜덤 파일명 생성
            String savedFileNm = FileUtils.makeRandomFileNm(img.getOriginalFilename());
            System.out.println("savedFileNm : " + savedFileNm);

            // 랜덤파일명을 entity에 넣음
            entity.setPlantPic(savedFileNm);
            MAPPER.insPlant(entity);

            // pk값 얻기
            System.out.println("result : " + result);
            System.out.println("pk : " + entity.getIplant());

            // D:/download/growgreen/plant/pk 값 폴더 생성
            String targetDir = String.format("%s/plant/%d", FileUtils.getAbsoluteDownloadPath(fileDir), entity.getIplant());
            File fileTargetDir = new File(targetDir);

            // 없으면 폴더 만든다
            if(!fileTargetDir.exists()) {
                fileTargetDir.mkdirs();
            }

            // 이미지를 해당 폴더로 옮긴다
            File fileTarget = new File(targetDir + "/" + savedFileNm);
            try {
                img.transferTo(fileTarget);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            PlantEntity entity = new PlantEntity();
            entity.setNm(dto.getNm());
            entity.setNickNm(dto.getNickNm());
            entity.setOnDate(dto.getOnDate());
            entity.setCtnt(dto.getCtnt());
            result = MAPPER.insPlantExPic(entity);

            return result;

        }
        return 1;
    }





    public int updPlant(MultipartFile img, PlantUpdDto dto) {
        if(img != null){
            String centerPath = String.format("plant/%d", dto.getIplant());
            String dicPath = String.format("%s/%s", FileUtils.getAbsoluteDownloadPath(fileDir), centerPath);
            File dic = new File(dicPath);
            if(!dic.exists()) {
                dic.mkdirs();
            }
            String savedFileName = FileUtils.makeRandomFileNm(img.getOriginalFilename());
            String savedFilePath = String.format("%s/%s", centerPath, savedFileName);
            String targetPath = String.format("%s/%s", FileUtils.getAbsoluteDownloadPath(fileDir), savedFilePath);

            File target = new File(targetPath);
            try {
                img.transferTo(target);
            }catch (Exception e) {
                return 0;
            }
            PlantEntity entity = new PlantEntity();
            entity.setPlantPic(savedFileName);
            entity.setIplant(dto.getIplant());
            entity.setNm(dto.getNm());
            entity.setNickNm(dto.getNickNm());
            entity.setCtnt(dto.getCtnt());
            entity.setOnDate(dto.getOnDate());

            try {
                int result = MAPPER.updPlant(entity);
                if(result == 0) {
                    throw new Exception("사진을 수정할 수 없습니다.");
                }
            } catch (Exception e) {
                //파일 삭제
                target.delete();
                return 0;
            }
        }

        PlantEntity entity = new PlantEntity();
        entity.setIplant(dto.getIplant());
        entity.setNm(dto.getNm());
        entity.setNickNm(dto.getNickNm());
        entity.setCtnt(dto.getCtnt());
        entity.setOnDate(dto.getOnDate());
        MAPPER.updPlantExPic(entity);

        return 1;
    }




    public int delPlant(PlantDelDto dto) {
        return MAPPER.delPlant(dto);
    }

    public List<PlantVo> selPlant() {
        return MAPPER.selPlant();
    }

    public PlantSelDetailVo selPlantDetail(int iplant) {
        return MAPPER.selPlantDetail(iplant);
    }


}
