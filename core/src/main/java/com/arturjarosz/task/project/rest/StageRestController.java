package com.arturjarosz.task.project.rest;

import com.arturjarosz.task.project.application.StageApplicationService;
import com.arturjarosz.task.project.application.dto.StageBasicDto;
import com.arturjarosz.task.project.application.dto.StageDto;
import com.arturjarosz.task.sharedkernel.model.CreatedEntityDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("projects")
public class StageRestController {

    private StageApplicationService stageApplicationService;

    public StageRestController(StageApplicationService stageApplicationService) {
        this.stageApplicationService = stageApplicationService;
    }

    @PostMapping("{projectId}/stages")
    public ResponseEntity<CreatedEntityDto> createStage(@PathVariable("projectId") Long projectId,
                                                        @RequestBody StageDto stageDto) {
        return new ResponseEntity<>(this.stageApplicationService.createStage(projectId, stageDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{projectId}/stages/{stageId}")
    public ResponseEntity<Void> removeStage(@PathVariable("projectId") Long projectId,
                                            @PathVariable("stageId") Long stageId) {
        this.stageApplicationService.removeStage(projectId, stageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{projectId}stages/{stageId}")
    public ResponseEntity<Void> updateStage(@PathVariable("projectId") Long projectId,
                                            @PathVariable("stageId") Long stageId, @RequestBody StageDto stageDto) {
        this.stageApplicationService.updateStage(projectId, stageId, stageDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("stages/{stageId}")
    public ResponseEntity<StageDto> getStage(@PathVariable("stageId") Long stageId) {
        return new ResponseEntity<>(this.stageApplicationService.getStage(stageId), HttpStatus.OK);
    }

    @GetMapping("{projectId}/stages/getBasic")
    public ResponseEntity<List<StageBasicDto>> getBasicStages(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(this.stageApplicationService.getStageBasicList(projectId), HttpStatus.OK);
    }

}