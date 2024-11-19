package com.hado90.processLogManagement;

import java.util.ArrayList;
import java.util.List;
import com.hado90.processLogManagement.ProcessLogSubscriber;


public class ProcessLogPublisher {
    private final List<ProcessLog> processLogs = new ArrayList<>();
    private ProcessLogSubscriber processLogSubscriber;

    public void addProcessLogSubscriber(ProcessLogSubscriber processLogSubscriber) {
        this.processLogSubscriber = processLogSubscriber;
    }

    public void pushProgressLog(String processLogStatus, String processLogMessage, List<String> processLogDetails) {
        ProcessLog log = new ProcessLog(processLogStatus, processLogMessage, processLogDetails);
        processLogs.add(log);
        if (processLogSubscriber != null) { processLogSubscriber. }
    }

    public ProcessLogIterator getProcessLogIterator() {
        return new ProcessLogIterator(processLogs);
    }
}
