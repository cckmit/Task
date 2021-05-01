package com.arturjarosz.task.client.application;

import com.arturjarosz.task.client.application.dto.ClientDto;

import java.util.List;

public interface ClientApplicationService {

    /**
     * Creates {@link com.arturjarosz.task.client.model.Client} from given {@link ClientDto}.
     * When not all data provided, then {@link com.arturjarosz.task.sharedkernel.exceptions.IllegalArgumentException}
     * is thrown.
     *
     * @param clientDto
     * @return
     */
    ClientDto createClient(ClientDto clientDto);

    /**
     * Loads basic data for {@link com.arturjarosz.task.client.model.Client} by given Id.
     * When not all data provided, then {@link com.arturjarosz.task.sharedkernel.exceptions.IllegalArgumentException}
     * is thrown.
     *
     * @param clientId
     * @return
     */
    ClientDto getClientBasicData(Long clientId);

    /**
     * Removes {@link com.arturjarosz.task.client.model.Client} by given Id.
     * When not all data provided, then {@link com.arturjarosz.task.sharedkernel.exceptions.IllegalArgumentException}
     * is thrown.
     *
     * @param clientId
     */
    void removeClient(Long clientId);

    /**
     * Loads all data for {@link com.arturjarosz.task.client.model.Client} by given Id.
     * When not all data provided, then {@link com.arturjarosz.task.sharedkernel.exceptions.IllegalArgumentException}
     * is thrown.
     *
     * @param clientId
     * @return
     */
    ClientDto getClient(Long clientId);

    /**
     * Updates {@link com.arturjarosz.task.client.model.Client} of given Id be data provided in ClientDto.
     * When not all data provided, then {@link com.arturjarosz.task.sharedkernel.exceptions.IllegalArgumentException}
     * is thrown.
     *
     * @param clientId
     * @param clientDto
     */
    ClientDto updateClient(Long clientId, ClientDto clientDto);

    /**
     * Loads list of basic clients data or all existing clients.
     *
     * @return
     */
    List<ClientDto> getBasicClients();
}
