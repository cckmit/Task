package com.arturjarosz.task.project.status.task.listener.impl

import com.arturjarosz.task.project.model.Project
import com.arturjarosz.task.project.model.Stage
import com.arturjarosz.task.project.model.Task
import com.arturjarosz.task.project.status.stage.StageStatus
import com.arturjarosz.task.project.status.stage.impl.StageWorkflowServiceImpl
import com.arturjarosz.task.project.status.task.TaskStatus
import com.arturjarosz.task.project.utils.ProjectBuilder
import com.arturjarosz.task.project.utils.StageBuilder
import com.arturjarosz.task.project.utils.TaskBuilder
import spock.lang.Specification

class TaskBackToToDoListenerTest extends Specification {
    private static final long STAGE_ID = 100L

    def stageWorkflowService = Mock(StageWorkflowServiceImpl)
    def taskBackToToDoListener = new TaskBackToToDoListener(stageWorkflowService)

    def "Changing status of the only task from IN_PROGRESS to TO_DO on the stage in IN_PROGRESS status should change that stage status to TO_DO"() {
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS, Arrays.asList(task))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            1 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, StageStatus.TO_DO)
    }

    def "Changing status of task from IN_PROGRESS to TO_DO on stage, where rest of tasks are in REJECTED, should change stage status to TO_DO"() {
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task2 = this.createTaskOfGivenStatus(TaskStatus.REJECTED)
            def task3 = this.createTaskOfGivenStatus(TaskStatus.REJECTED)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS,
                            Arrays.asList(task, task2, task3))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            1 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, StageStatus.TO_DO)
    }

    def "Changing status of task from IN_PROGRESS to TO_DO on stage, where rest of tasks are in TO_DO and REJECTED, should change stage status to TO_DO"() {
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task2 = this.createTaskOfGivenStatus(TaskStatus.TO_DO)
            def task3 = this.createTaskOfGivenStatus(TaskStatus.REJECTED)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS,
                            Arrays.asList(task, task2, task3))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            1 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, StageStatus.TO_DO)
    }

    def "Changing status of task from IN_PROGRESS to TO_DO on stage, where rest of tasks are in TO_DO, should change stage status to TO_DO"() {
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task2 = this.createTaskOfGivenStatus(TaskStatus.TO_DO)
            def task3 = this.createTaskOfGivenStatus(TaskStatus.TO_DO)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS,
                            Arrays.asList(task, task2, task3))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            1 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, StageStatus.TO_DO)
    }

    def "Changing status of task from IN_PROGRESS to TO_DO on stage, where at least on the tasks is in DONE, should not change stage status"(){
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task2 = this.createTaskOfGivenStatus(TaskStatus.DONE)
            def task3 = this.createTaskOfGivenStatus(TaskStatus.TO_DO)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS,
                            Arrays.asList(task, task2, task3))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            0 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, _ as StageStatus)
    }
    def "Changing status of task from IN_PROGRESS to TO_DO on stage, where at least on the tasks is in IN_PROGRESS, should not change stage status"(){
        given:
            def task = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task2 = this.createTaskOfGivenStatus(TaskStatus.IN_PROGRESS)
            def task3 = this.createTaskOfGivenStatus(TaskStatus.TO_DO)
            def stage =
                    this.createStageWithIdStatusAndGivenTasks(STAGE_ID, StageStatus.IN_PROGRESS,
                            Arrays.asList(task, task2, task3))
            def project = this.createProjectWithGivenStage(stage)
        when:
            task.changeStatus(TaskStatus.TO_DO)
            this.taskBackToToDoListener.onTaskStatusChange(project, STAGE_ID)
        then:
            0 * this.stageWorkflowService.changeStageStatusOnProject(project, STAGE_ID, _ as StageStatus)
    }

    private Project createProjectWithGivenStage(Stage stage) {
        new ProjectBuilder().withStage(stage).build()
    }

    private Stage createStageWithIdStatusAndGivenTasks(long stageId, StageStatus stageStatus, List<Task> tasks) {
        new StageBuilder().withId(stageId).withStatus(stageStatus).withTasks(tasks).build()
    }

    private Task createTaskOfGivenStatus(TaskStatus taskStatus) {
        new TaskBuilder().withStatus(taskStatus).build()
    }
}
