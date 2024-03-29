package com.arturjarosz.task.finance.domain.dto;

import java.math.BigDecimal;

public class FinancialDataDto {
    public static final String HAS_INVOICE = "hasInvoice";
    public static final String PAYABLE = "payable";
    public static final String PAID = "paid";
    public static final String VALUE = "value";

    private boolean hasInvoice;
    private boolean payable;
    private boolean paid;
    private BigDecimal value;

    public FinancialDataDto() {
    }

    public boolean isHasInvoice() {
        return this.hasInvoice;
    }

    public void setHasInvoice(boolean hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public boolean isPayable() {
        return this.payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
