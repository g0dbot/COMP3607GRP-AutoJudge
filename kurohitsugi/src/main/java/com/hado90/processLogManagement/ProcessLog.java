package com.hado90.processLogManagement;

import java.util.List;

public class ProcessLog {
    private final String processLogStatus;
    private final String processLogMessage;
    private final List<String> processLogDetails;

    public ProcessLog(String processLogStatus, String processLogMessage, List<String> processLogDetails) {
        this.processLogStatus = processLogStatus;
        this.processLogMessage = processLogMessage;
        this.processLogDetails = processLogDetails;
    }

    // Getters
    public String getProcessLogStatus() {
        return processLogStatus;
    }

    public String getProcessLogMessage() {
        return processLogMessage;
    }

    public List<String> getProcessLogDetails() {
        return processLogDetails;
    }

    @Override
    public String toString() {
        return "ProcessLog{" +
                "status='" + processLogStatus + '\'' +
                ", message='" + processLogMessage + '\'' +
                ", details=" + processLogDetails +
                '}';
    }
}