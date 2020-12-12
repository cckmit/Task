package com.arturjarosz.task.client.application.dto;

import com.arturjarosz.task.client.model.ClientType;

public class ClientBasicDto {

    private Long id;
    private ClientType clientType;
    private String firstName;
    private String lastName;
    private String companyName;

    public ClientBasicDto(Long id, ClientType clientType, String firstName, String lastName, String companyName) {
        this.id = id;
        this.clientType = clientType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}