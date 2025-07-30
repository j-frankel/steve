package de.rwth.idsg.steve.web.dto.internal;

public class MeterValuesAlignedDataResponse {

    private boolean accepted;
    private int     taskId;
    private String  message;

    public boolean isAccepted() {
        return accepted;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}