package com.arturjarosz.task.project.query;

import com.arturjarosz.task.project.application.dto.StageDto;
import com.arturjarosz.task.project.application.dto.SupervisionDto;
import com.arturjarosz.task.project.application.dto.SupervisionVisitDto;
import com.arturjarosz.task.project.application.dto.TaskDto;
import com.arturjarosz.task.project.model.CooperatorJob;
import com.arturjarosz.task.project.model.Cost;
import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.project.model.Stage;
import com.arturjarosz.task.project.model.Supervision;

import java.util.List;

public interface ProjectQueryService {

    /**
     * Load Cost by given costId.
     *
     * @param costId
     * @return
     */
    Cost getCostById(Long costId);

    /**
     * Load Stage by given stageId.
     *
     * @param stageId
     * @return
     */
    Stage getStageById(Long stageId);

    /**
     * Load CooperatorJob by given cooperatorJobId.
     *
     * @param cooperatorJobId
     * @return
     */
    CooperatorJob getCooperatorJobByIdForProject(Long cooperatorJobId);

    /**
     * Load list of Project for given Client with clientId.
     *
     * @param clientId
     * @return
     */
    List<Project> getProjectsForClientId(Long clientId);

    /**
     * Load list of Project for given Architect with architectId.
     *
     * @param architectId
     * @return
     */
    List<Project> getProjectsForArchitect(Long architectId);

    /**
     * Retrieve Task as TaskDto of given TaskId.
     *
     * @return
     */
    TaskDto getTaskByTaskId(Long taskId);

    /**
     * Return List of Stages as StageDto for Project of given projectId.
     *
     * @param projectId
     * @return
     */
    List<StageDto> getStagesForProjectById(Long projectId);

    /**
     * Returns Supervision for Project of given projectId.
     *
     * @return
     */
    Supervision getSupervision(Long projectId);

    /**
     * Checks if Project of given projectId has Supervision.
     *
     * @param projectId
     * @return
     */
    boolean projectHasSupervision(Long projectId);

    /**
     * Retrieves Supervision of Project.
     *
     * @param projectId
     * @return
     */
    SupervisionDto getProjectSupervision(Long projectId);

    /**
     * Retrieves SupervisionVisit for Project.
     *
     * @param supervisionVisitId
     * @return
     */
    SupervisionVisitDto getProjectSupervisionVisit(Long projectId, Long supervisionVisitId);
}
