package com.taskManager.modal;

public enum TaskStatus {
    PENDING("PENDING"),
    ASSIGNED("ASSIGNED"),
    DONE("DONE");

    private String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
