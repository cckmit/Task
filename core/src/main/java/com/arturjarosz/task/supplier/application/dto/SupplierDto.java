package com.arturjarosz.task.supplier.application.dto;

import com.arturjarosz.task.supplier.model.SupplierCategory;

import java.io.Serializable;

public class SupplierDto implements Serializable {
    private static final long serialVersionUID = -3835889627615624342L;

    private Long id;
    private String name;
    private String note;
    private String email;
    private String telephone;
    private Double jobsValue;
    private SupplierCategory category;

    public SupplierDto() {
        //needed by Hibernate
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getJobsValue() {
        return this.jobsValue;
    }

    public void setJobsValue(Double jobsValue) {
        this.jobsValue = jobsValue;
    }

    public SupplierCategory getCategory() {
        return this.category;
    }

    public void setCategory(SupplierCategory category) {
        this.category = category;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
