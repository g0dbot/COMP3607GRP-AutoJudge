package com.hado90.submissionMgt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Iterator;

public class SubmissionManager {
    private HashMap<String, Submission> submissions = new HashMap<>();

    public boolean submissionAlreadyExists(String submissionId) { return submissions.containsKey(submissionId); }

    public Submission getSubmission(String submissionId) { return submissions.get(submissionId); }

    public void addSubmission(String submissionPath) { 
        Submission submission = new Submission(submissionPath);
        if (!submissionAlreadyExists(submission.getSubmissionId())) { submissions.put(submission.getSubmissionId(), submission); }
    }

    public void bulkAddSubmissions(List<String> submissionPaths) { submissionPaths.forEach(this::addSubmission); }

    public void getAllSubmissions() { 
        submissions.forEach((submissionId, submission) -> 
        System.out.println("Submission Path: " + submission.getSubmissionPath()));
    }

    public Map<String, Submission> filterSubmissionsByStatus(String status) {
        return submissions.entrySet()
                          .stream()
                          .filter(entry -> entry.getValue().getSubmissionStatus().equals(status))
                          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //INTERNAL ITERATOR
    public static class SubmissionIterator implements Iterable<Submission> {
        private final Map<String, Submission> submissions;
        private final String statusFilter;
    
        public SubmissionIterator(Map<String, Submission> submissions, String statusFilter) {
            this.submissions = submissions;
            this.statusFilter = statusFilter;
        }
    
        @Override
        public Iterator<Submission> iterator() {
            return new Iterator<Submission>() {
                private final Iterator<Map.Entry<String, Submission>> iterator = submissions.entrySet().iterator();
                private Submission nextMatch;
    
                private void roll() {
                    nextMatch = null;
                    while (iterator.hasNext()) {  Map.Entry<String, Submission> entry = iterator.next();
                        if (entry.getValue().getSubmissionStatus().equals(statusFilter)) {
                            nextMatch = entry.getValue();
                            break;
                        }
                    }
                }
    
                @Override
                public boolean hasNext() {
                    if (nextMatch == null) { roll(); }
                    return nextMatch != null;
                }
    
                @Override
                public Submission next() {
                    if (nextMatch == null) { roll(); }
                    if (nextMatch == null) { throw new IllegalStateException("No more elements"); }
                    Submission current = nextMatch;
                    nextMatch = null;
                    return current;
                }
            };
        }
    }
}
