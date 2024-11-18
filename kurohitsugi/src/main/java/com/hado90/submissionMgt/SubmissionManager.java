package com.hado90.submissionMgt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
}
