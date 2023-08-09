package com.green.growgreen.plant;

import com.green.growgreen.plant.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlantMapper {
    int insPlant(PlantEntity entity);
    int insPlantExPic(PlantEntity entity);

    int updPlant(PlantEntity entity);
    int updPlantExPic(PlantEntity entity);

    int delPlant(PlantDelDto dto);


    List<PlantVo> selPlant();
    PlantSelDetailVo selPlantDetail(int iplant);
}
