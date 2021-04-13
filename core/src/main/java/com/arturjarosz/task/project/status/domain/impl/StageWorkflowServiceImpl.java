package com.arturjarosz.task.project.status.domain.impl;

import com.arturjarosz.task.project.application.ProjectExceptionCodes;
import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.project.model.Stage;
import com.arturjarosz.task.project.status.domain.StageStatus;
import com.arturjarosz.task.project.status.domain.StageStatusTransition;
import com.arturjarosz.task.project.status.domain.StageWorkflowService;
import com.arturjarosz.task.project.status.domain.validator.StageStatusTransitionValidator;
import com.arturjarosz.task.sharedkernel.annotations.ApplicationService;
import com.arturjarosz.task.sharedkernel.exceptions.BaseValidator;
import com.arturjarosz.task.sharedkernel.exceptions.ExceptionCodes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationService
public class StageWorkflowServiceImpl implements StageWorkflowService {

    private Map<String, List<StageStatusTransitionValidator>> mapNameToStatusTransitionValidators;

    @Autowired
    public StageWorkflowServiceImpl(List<StageStatusTransitionValidator> transitionValidatorList) {
        this.mapNameToStatusTransitionValidators = new HashMap<>();
        this.mapNameToStatusTransitionValidators = transitionValidatorList.stream()
                .collect(Collectors.groupingBy(
                        stageStatusTransitionValidator -> stageStatusTransitionValidator.getStatusTransition().getName()
                ));
    }

    @Override
    public void changeStageStatusOnProject(Project project, Long stageId, StageStatus newStatus) {
        Predicate<Stage> stagePredicate = stage -> stage.getId().equals(stageId);
        Stage stage = project.getStages().stream()
                .filter(stagePredicate)
                .findFirst().orElse(null);
        this.changeStatus(stage, newStatus);
    }

    @Override
    public void changeStatus(Stage stage, StageStatus status) {
        StageStatusTransition stageStatusTransition = this.getTransitionForStatuses(stage.getStatus(), status);
        BaseValidator.assertNotNull(stageStatusTransition, BaseValidator.createMessageCode(ExceptionCodes.NOT_VALID,
                ProjectExceptionCodes.STAGE, ProjectExceptionCodes.STATUS, ProjectExceptionCodes.TRANSITION,
                stage.getStatus().getStatusName(), status.getStatusName()));
        this.beforeStatusChange(stage, stageStatusTransition);
        stage.changeStatus(status);
        this.afterStatusChange(stage, stageStatusTransition);
    }

    @Override
    public void beforeStatusChange(Stage stage, StageStatusTransition statusTransition) {
        List<StageStatusTransitionValidator> validators = this.getStatusTransitionValidators(statusTransition);
        validators.forEach(validator -> validator.validate(stage, statusTransition));
    }

    @Override
    public void afterStatusChange(Stage stage, StageStatusTransition statusTransition) {
        // TODO: run loggers
        // TODO: run listeners for status change on stage
    }

    private StageStatusTransition getTransitionForStatuses(StageStatus oldStatus, StageStatus newStatus) {
        return Arrays.stream(StageStatusTransition.values())
                .filter(transition -> transition.getCurrentStatus().equals(oldStatus) && transition.getNextStatus()
                        .equals(newStatus)).findFirst().orElse(null);
    }

    private List<StageStatusTransitionValidator> getStatusTransitionValidators(StageStatusTransition statusTransition) {
        List<StageStatusTransitionValidator> validators = this.mapNameToStatusTransitionValidators
                .get(statusTransition.getName());
        if (validators == null) {
            return Collections.emptyList();
        }
        return validators;
    }
}
