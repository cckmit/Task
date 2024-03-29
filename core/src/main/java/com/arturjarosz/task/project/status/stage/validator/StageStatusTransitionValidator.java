package com.arturjarosz.task.project.status.stage.validator;

import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.project.model.Stage;
import com.arturjarosz.task.project.status.stage.StageStatusTransition;
import com.arturjarosz.task.sharedkernel.status.StatusTransitionValidator;

public interface StageStatusTransitionValidator extends StatusTransitionValidator<StageStatusTransition> {

    /**
     * Validate if planned statusTransition for Stage on given Project can be executed.
     */
    void validate(Project project, Stage stage, StageStatusTransition statusTransition);
}
