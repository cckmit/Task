package com.arturjarosz.task.project.application.dto;

import com.arturjarosz.task.architect.application.dto.ArchitectBasicDto;
import com.arturjarosz.task.client.application.dto.ClientBasicDto;
import com.arturjarosz.task.project.model.ProjectType;

import java.io.Serializable;
import java.time.LocalDate;

public class ProjectDto implements Serializable {
    private static final long serialVersionUID = 8501354213746117568L;

    private ClientBasicDto client;
    private ArchitectBasicDto architect;
    private String name;
    private ProjectType projectType;
    private LocalDate signingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate deadline;
    private String note;

    public ProjectDto() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectType getProjectType() {
        return this.projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ClientBasicDto getClient() {
        return this.client;
    }

    public void setClient(ClientBasicDto client) {
        this.client = client;
    }

    public ArchitectBasicDto getArchitect() {
        return this.architect;
    }

    public void setArchitect(ArchitectBasicDto architect) {
        this.architect = architect;
    }

    public LocalDate getSigningDate() {
        return this.signingDate;
    }

    public void setSigningDate(LocalDate signingDate) {
        this.signingDate = signingDate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}