package com.green.growgreen.plant;

import com.green.growgreen.plant.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService SERVICE;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "식물 등록")
    public int postPlant(@RequestPart(required = false) MultipartFile img
                         ,@RequestPart PlantInsDto dto){
        return SERVICE.insPlant(img, dto);
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "식물 수정")
    public int putPlant(@RequestPart(required = false) MultipartFile img
                         ,@RequestPart PlantUpdDto dto){
        return SERVICE.updPlant(img, dto);
    }

    @DeleteMapping
    @Operation(summary = "식물 삭제")
    public int deletePlant(@RequestBody PlantDelDto dto) {
        return SERVICE.delPlant(dto);
    }



    @GetMapping
    @Operation(summary = "모든 식물 리스트 보기 ")
    public List<PlantVo> getPlant() {
        return SERVICE.selPlant();
    }

    @GetMapping("/{iplant}")
    @Operation(summary = "특정 식물 보기")
    public PlantSelDetailVo selPlantDetail(@PathVariable int iplant) {
        return SERVICE.selPlantDetail(iplant);
    }

}
