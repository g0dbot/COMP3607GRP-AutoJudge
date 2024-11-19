package com.hado90.processLogMgt;

public class ProcessLogSubscriber {
    private final ProcessLogPublisher processLogPublisher;

    public ProcessLogSubscriber(ProcessLogPublisher processLogPublisher) {
        this.processLogPublisher = processLogPublisher;
        this.processLogPublisher.addProcessLogSubscriber(this);
    }

    public void updateLog() {
        ProcessLogIterator iterator = processLogPublisher.getProcessLogIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
    
    private void updateProgressBar(String status) { System.out.println("Updating Progress Bar: " + status); }
    private void updateProgressMessage(String message) { }
}
