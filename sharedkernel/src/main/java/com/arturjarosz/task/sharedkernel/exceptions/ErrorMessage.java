package com.arturjarosz.task.sharedkernel.exceptions;

public class ErrorMessage {
    private String message;
    private Object[] parameters;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getMessage() {
        return this.message;
    }

    public Object[] getParameters() {
        return this.parameters;
    }
}
