package com.arturjarosz.task.project.status.domain.impl;

import com.arturjarosz.task.project.application.ProjectExceptionCodes;
import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.project.model.Stage;
import com.arturjarosz.task.project.model.Task;
import com.arturjarosz.task.project.status.domain.TaskStatus;
import com.arturjarosz.task.project.status.domain.TaskStatusTransition;
import com.arturjarosz.task.project.status.domain.TaskWorkflowService;
import com.arturjarosz.task.project.status.domain.listener.TaskStatusTransitionListener;
import com.arturjarosz.task.project.status.domain.validator.TaskStatusTransitionValidator;
import com.arturjarosz.task.sharedkernel.annotations.DomainService;
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

@DomainService
public class TaskWorkflowServiceImpl implements TaskWorkflowService {

    private Map<String, List<TaskStatusTransitionValidator>> mapNameToStatusTransitionValidators;
    private Map<String, List<TaskStatusTransitionListener>> mapNameToStatusTransitionListeners;

    @Autowired
    public TaskWorkflowServiceImpl(List<TaskStatusTransitionListener> taskStatusTransitionListenerList,
                                   List<TaskStatusTransitionValidator> transitionValidatorList) {
        this.mapNameToStatusTransitionValidators = new HashMap<>();
        this.mapNameToStatusTransitionValidators = transitionValidatorList.stream()
                .collect(Collectors.groupingBy(
                        taskStatusTransitionValidator -> taskStatusTransitionValidator.getStatusTransition()
                                .getName()));
        this.mapNameToStatusTransitionListeners = new HashMap<>();
        this.mapNameToStatusTransitionListeners = taskStatusTransitionListenerList.stream()
                .collect(Collectors.groupingBy(
                        taskStatusTransitionListener -> taskStatusTransitionListener.getStatusTransition().getName()
                ));
    }

    @Override
    public void changeStatus(Task task, TaskStatus newStatus) {
        task.changeStatus(newStatus);
    }

    @Override
    public void changeTaskStatusOnProject(Project project, Long stageId, Long taskId, TaskStatus newStatus) {
        Task task = this.getTask(project, stageId, taskId);
        /*
        In case of newly created Task, there is no status transition. For avoiding nullPointerException
        old status is set to TO_DO as well, as there is no status before.
         */
        TaskStatus oldStatus = task.getStatus() != null ? task.getStatus() : TaskStatus.TO_DO;
        TaskStatusTransition taskStatusTransition = this.getTransitionForStatuses(oldStatus, newStatus);
        BaseValidator.assertNotNull(taskStatusTransition, BaseValidator.createMessageCode(ExceptionCodes.NOT_VALID,
                ProjectExceptionCodes.TASK, ProjectExceptionCodes.STATUS, ProjectExceptionCodes.TRANSITION,
                oldStatus.getStatusName(), newStatus.getStatusName()));
        this.beforeStatusChange(project, task, stageId, taskStatusTransition);
        this.changeStatus(task, newStatus);
        this.afterStatusChange(project, stageId, taskStatusTransition);
    }

    @Override
    public void beforeStatusChange(Project project, Task task, Long stageId, TaskStatusTransition statusTransition) {
        List<TaskStatusTransitionValidator> validators = this.getStatusTransitionValidators(statusTransition);
        validators.forEach(validator -> validator.validate(project, task, stageId, statusTransition));
    }

    @Override
    public void afterStatusChange(Project project, Long stageId, TaskStatusTransition statusTransition) {
        List<TaskStatusTransitionListener> listenerList = this.getStatusTransitionListeners(statusTransition);
        listenerList.forEach(listener -> listener.onTaskStatusChange(project, stageId));
        // TODO: run loggers
    }

    private List<TaskStatusTransitionListener> getStatusTransitionListeners(TaskStatusTransition statusTransition) {
        List<TaskStatusTransitionListener> listeners = this.mapNameToStatusTransitionListeners
                .get(statusTransition.getName());
        if (listeners == null) {
            return Collections.emptyList();
        }
        return listeners;
    }

    private List<TaskStatusTransitionValidator> getStatusTransitionValidators(TaskStatusTransition statusTransition) {
        List<TaskStatusTransitionValidator> validators = this.mapNameToStatusTransitionValidators
                .get(statusTransition.getName());
        if (validators == null) {
            return Collections.emptyList();
        }
        return validators;
    }

    private TaskStatusTransition getTransitionForStatuses(TaskStatus oldStatus, TaskStatus newStatus) {
        return Arrays.stream(TaskStatusTransition.values())
                .filter(transition -> transition.getCurrentStatus().equals(oldStatus) && transition.getNextStatus()
                        .equals(newStatus)).findFirst().orElse(null);
    }

    private Task getTask(Project project, Long stageId, Long taskId) {
        Predicate<Stage> stagePredicate = stage -> stage.getId().equals(stageId);
        //Newly created Task do not have assigned ids yet.
        Predicate<Task> taskPredicate = taskId != null ? task -> task.getId().equals(taskId) : task -> task
                .getId() == null;
        return project.getStages().stream()
                .filter(stagePredicate)
                .flatMap(stage -> stage.getTasks().stream())
                .filter(taskPredicate)
                .findFirst().orElse(null);
    }
}