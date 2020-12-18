package com.arturjarosz.task.project.model;

import com.arturjarosz.task.sharedkernel.model.AbstractAggregateRoot;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "PROJECT")
public class Project extends AbstractAggregateRoot {
    private static final long serialVersionUID = 5437961881026141924L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ARCHITECT_ID", nullable = false)
    private Long architectId;

    @Column(name = "CLIENT_ID", nullable = false)
    private Long clientId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT_ID")
    private Set<Cost> costs;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT_ID")
    private Set<Stage> stages;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_TYPE")
    private ProjectType projectType;

    protected Project() {
        //needed by Hibernate
    }

    public Project(String name, Long architectId, Long clientId, ProjectType projectType) {
        this.name = name;
        this.architectId = architectId;
        this.clientId = clientId;
        this.projectType = projectType;
    }

    public String getName() {
        return this.name;
    }

    public Long getArchitectId() {
        return this.architectId;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public Set<Cost> getCosts() {
        return this.costs;
    }

    public Set<Stage> getStages() {
        return this.stages;
    }

    public ProjectType getProjectType() {
        return this.projectType;
    }
}
