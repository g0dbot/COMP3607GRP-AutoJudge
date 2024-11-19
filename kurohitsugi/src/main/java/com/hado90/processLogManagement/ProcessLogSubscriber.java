package com.hado90.processLogManagement;

public class ProcessLogSubscriber {
    private final ProcessLogPublisher processLogPublisher;

    public ProcessLogSubscriber(ProcessLogPublisher processLogPublisher) {
        this.processLogPublisher = processLogPublisher;
        this.processLogPublisher.addProcessLogSubscriber(this);
    }

    public void updateLog() {
        System.out.println("New process logs received:");
        ProcessLogIterator iterator = processLogPublisher.getProcessLogIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
