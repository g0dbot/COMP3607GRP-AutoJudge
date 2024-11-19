package com.hado90.processLogManagement;

import java.util.Iterator;
import java.util.List;

public class ProcessLogIterator implements Iterator<ProcessLog> {
    private final List<ProcessLog> processLogs;
    private int index = 0;

    public ProcessLogIterator(List<ProcessLog> processLogs) {
        this.processLogs = processLogs;
    }

    @Override
    public boolean hasNext() {
        return index < processLogs.size();
    }

    @Override
    public ProcessLog next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements");
        }
        return processLogs.get(index++);
    }
}