package com.arturjarosz.task.project.application.impl;

import com.arturjarosz.task.architect.application.ArchitectApplicationService;
import com.arturjarosz.task.architect.application.ArchitectValidator;
import com.arturjarosz.task.architect.application.dto.ArchitectDto;
import com.arturjarosz.task.client.application.ClientApplicationService;
import com.arturjarosz.task.client.application.ClientValidator;
import com.arturjarosz.task.client.application.dto.ClientDto;
import com.arturjarosz.task.contract.application.ContractService;
import com.arturjarosz.task.contract.application.dto.ContractDto;
import com.arturjarosz.task.contract.application.mapper.ContractDtoMapper;
import com.arturjarosz.task.contract.model.Contract;
import com.arturjarosz.task.finance.application.ProjectFinancialDataService;
import com.arturjarosz.task.project.application.ProjectApplicationService;
import com.arturjarosz.task.project.application.ProjectValidator;
import com.arturjarosz.task.project.application.dto.ProjectCreateDto;
import com.arturjarosz.task.project.application.dto.ProjectDto;
import com.arturjarosz.task.project.application.mapper.ProjectDtoMapper;
import com.arturjarosz.task.project.domain.ProjectDomainService;
import com.arturjarosz.task.project.infrastructure.repositor.ProjectRepository;
import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.sharedkernel.annotations.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationService
public class ProjectApplicationServiceImpl implements ProjectApplicationService {
    public static final Logger LOG = LoggerFactory.getLogger(ProjectApplicationServiceImpl.class);

    private final ClientApplicationService clientApplicationService;
    private final ClientValidator clientValidator;
    private final ArchitectApplicationService architectApplicationService;
    private final ArchitectValidator architectValidator;
    private final ProjectRepository projectRepository;
    private final ProjectDomainService projectDomainService;
    private final ProjectValidator projectValidator;
    private final ProjectFinancialDataService projectFinancialDataService;
    private final ContractService contractService;

    @Autowired
    public ProjectApplicationServiceImpl(ClientApplicationService clientApplicationService, ClientValidator clientValidator,
            ArchitectApplicationService architectApplicationService, ArchitectValidator architectValidator, ProjectRepository projectRepository,
            ProjectDomainService projectDomainService, ProjectValidator projectValidator,
            ProjectFinancialDataService projectFinancialDataService, ContractService contractService) {
        this.clientApplicationService = clientApplicationService;
        this.clientValidator = clientValidator;
        this.architectApplicationService = architectApplicationService;
        this.architectValidator = architectValidator;
        this.projectRepository = projectRepository;
        this.projectDomainService = projectDomainService;
        this.projectValidator = projectValidator;
        this.projectFinancialDataService = projectFinancialDataService;
        this.contractService = contractService;
    }

    @Transactional
    @Override
    public ProjectDto createProject(ProjectCreateDto projectCreateDto) {
        LOG.debug("Creating Project.");
        this.projectValidator.validateProjectBasicDto(projectCreateDto);
        this.architectValidator.validateArchitectExistence(projectCreateDto.getArchitectId());
        this.clientValidator.validateClientExistence(projectCreateDto.getClientId());
        ContractDto contractDto = ContractDtoMapper.INSTANCE.projectDtoToContractDto(projectCreateDto);
        Contract contract = this.contractService.createContract(contractDto);
        Project project = this.projectDomainService.createProject(projectCreateDto, contract.getId());
        project = this.projectRepository.save(project);
        this.projectFinancialDataService.createProjectFinancialData(project.getId());
        LOG.debug("Project created.");
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(project, contract);
    }

    @Override
    public ProjectDto getProject(Long projectId) {
        LOG.debug("Loading Project with id {}.", projectId);
        this.projectValidator.validateProjectExistence(projectId);
        Project project = this.projectRepository.load(projectId);
        ClientDto clientDto = this.clientApplicationService.getClientBasicData(project.getClientId());
        ArchitectDto architectDto = this.architectApplicationService.getArchitect(project.getArchitectId());
        LOG.debug("Project with id {} loaded.", projectId);
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(clientDto, architectDto, project);
    }

    @Transactional
    @Override
    public ProjectDto updateProject(Long projectId, ProjectDto projectDto) {
        LOG.debug("Updating Project with id {}.", projectId);
        Project project = this.projectRepository.load(projectId);
        this.projectValidator.validateProjectExistence(projectId);
        this.projectValidator.validateUpdateProjectDto(projectDto);
        project = this.projectDomainService.updateProject(project, projectDto);
        project = this.projectRepository.save(project);
        LOG.debug("Project with id {} updated", projectId);
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(project);
    }

    @Transactional
    @Override
    public void removeProject(Long projectId) {
        LOG.debug("Removing Project with id {}.", projectId);
        this.projectValidator.validateProjectExistence(projectId);
        this.projectFinancialDataService.removeFinancialDataForProject(projectId);
        this.projectRepository.remove(projectId);
        LOG.debug("Project with id {} removed.", projectId);
    }

    @Transactional
    @Override
    public ProjectDto finishProject(Long projectId, ProjectDto projectContractDto) {
        LOG.debug("Finishing Project with id {}.", projectId);
        //TODO: TA-62 update conditions on what project can be ended
        Project project = this.projectRepository.load(projectId);
        this.projectValidator.validateProjectExistence(projectId);
        project = this.projectDomainService.finishProject(project, projectContractDto.getEndDate());
        this.projectRepository.save(project);
        LOG.debug("Project with id {} is finished.", projectId);
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(project);
    }

    @Override
    public List<ProjectDto> getProjects() {
        LOG.debug("Loading list of projects.");
        return this.projectRepository.loadAll().stream().map(project -> {
            ClientDto clientDto = this.clientApplicationService.getClientBasicData(project.getClientId());
            ArchitectDto architectDto = this.architectApplicationService.getArchitect(project.getArchitectId());
            return ProjectDtoMapper.INSTANCE.projectToBasicProjectDto(clientDto, architectDto, project);
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProjectDto rejectProject(Long projectId) {
        LOG.debug("Rejecting Project with id {}.", projectId);
        this.projectValidator.validateProjectExistence(projectId);
        Project project = this.projectRepository.load(projectId);
        project = this.projectDomainService.rejectProject(project);
        this.projectRepository.save(project);
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(project);
    }

    @Transactional
    @Override
    public ProjectDto reopenProject(Long projectId) {
        LOG.debug("Reopening Project with id {}.", projectId);
        this.projectValidator.validateProjectExistence(projectId);
        Project project = this.projectRepository.load(projectId);
        project = this.projectDomainService.reopenProject(project);
        this.projectRepository.save(project);
        return ProjectDtoMapper.INSTANCE.projectToProjectDto(project);
    }
}
