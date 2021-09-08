package com.arturjarosz.task.project.status.task.listener.impl;

import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.stage.model.Stage;
import com.arturjarosz.task.stage.status.StageStatus;
import com.arturjarosz.task.stage.status.StageWorkflowService;
import com.arturjarosz.task.project.status.task.TaskStatusTransition;
import com.arturjarosz.task.project.status.task.listener.TaskStatusTransitionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskReopenListener implements TaskStatusTransitionListener {
    private final TaskStatusTransition statusTransition = TaskStatusTransition.REOPEN;
    private final StageWorkflowService stageWorkflowService;

    @Autowired
    public TaskReopenListener(StageWorkflowService stageWorkflowService) {
        this.stageWorkflowService = stageWorkflowService;
    }

    @Override
    public void onTaskStatusChange(Project project, Long stageId) {
        Stage stage = project.getStages().stream()
                .filter(stageOnProject -> stageOnProject.getId().equals(stageId))
                .findFirst().orElse(null);
        assert stage != null;
        if (stage.getStatus().equals(StageStatus.COMPLETED)) {
            this.stageWorkflowService
                    .changeStageStatusOnProject(project, stageId, StageStatus.TO_DO);
        }
    }

    @Override
    public TaskStatusTransition getStatusTransition() {
        return this.statusTransition;
    }
}
