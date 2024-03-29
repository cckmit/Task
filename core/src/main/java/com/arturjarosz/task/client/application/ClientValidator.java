package com.arturjarosz.task.client.application;

import com.arturjarosz.task.client.application.dto.ClientDto;
import com.arturjarosz.task.client.domain.ClientExceptionCodes;
import com.arturjarosz.task.client.infrastructure.repository.ClientRepository;
import com.arturjarosz.task.client.model.Client;
import com.arturjarosz.task.client.model.ClientType;
import com.arturjarosz.task.project.model.Project;
import com.arturjarosz.task.project.query.ProjectQueryService;
import com.arturjarosz.task.sharedkernel.exceptions.ExceptionCodes;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.arturjarosz.task.sharedkernel.exceptions.BaseValidator.*;

/**
 * Validates Client entity and Client related Dtos.
 */
@Component
public class ClientValidator {

    private final ClientRepository clientRepository;
    private final ProjectQueryService projectQueryService;

    public ClientValidator(ClientRepository clientRepository, ProjectQueryService projectQueryService) {
        this.clientRepository = clientRepository;
        this.projectQueryService = projectQueryService;
    }

    public void validateClientDtoPresence(ClientDto clientDto) {
        assertIsTrue(clientDto != null,
                createMessageCode(ExceptionCodes.NULL, ClientExceptionCodes.CLIENT));
    }

    public void validateClientBasicDto(ClientDto clientDto) {
        assertIsTrue(clientDto != null,
                createMessageCode(ExceptionCodes.NULL, ClientExceptionCodes.CLIENT));
        assertIsTrue(clientDto.getClientType() != null,
                createMessageCode(ExceptionCodes.NULL, ClientExceptionCodes.CLIENT,
                        ClientExceptionCodes.CLIENT_TYPE));
        if (clientDto.getClientType().equals(ClientType.CORPORATE)) {
            this.validateCorporateClient(clientDto);
        } else {
            this.validatePrivateClient(clientDto);
        }
    }

    public void validatePrivateClient(ClientDto clientDto) {
        this.validateName(clientDto.getFirstName(), ClientExceptionCodes.FIRST_NAME);
        this.validateName(clientDto.getLastName(), ClientExceptionCodes.LAST_NAME);
    }

    public void validateCorporateClient(ClientDto clientDto) {
        this.validateName(clientDto.getCompanyName(), ClientExceptionCodes.COMPANY_NAME);
    }

    public void validateClientExistence(Client client, Long clientId) {
        assertIsTrue(client != null,
                createMessageCode(ExceptionCodes.NOT_EXIST, ClientExceptionCodes.CLIENT), clientId);
    }

    private void validateName(String name, String nameExceptionCode) {
        assertIsTrue(name != null,
                createMessageCode(ExceptionCodes.NULL, ClientExceptionCodes.CLIENT,
                        nameExceptionCode));
        assertNotEmpty(name, createMessageCode(ExceptionCodes.EMPTY, ClientExceptionCodes.CLIENT, nameExceptionCode));
    }

    public void validateClientExistence(Long clientId) {
        Client client = this.clientRepository.load(clientId);
        this.validateClientExistence(client, clientId);
    }

    public void validateClientHasNoProjects(Long clientId) {
        List<Project> projects = this.projectQueryService.getProjectsForClientId(clientId);
        assertIsTrue(projects.isEmpty(),
                createMessageCode(ExceptionCodes.NOT_VALID, ClientExceptionCodes.CLIENT,
                        ClientExceptionCodes.PROJECTS));
    }

}
