package com.hado90.submissionMgt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Iterator;

/**
 * Manages a collection of submissions, providing functionality to add, retrieve, 
 * and filter submissions by their status. The class also includes an internal iterator 
 * for traversing submissions based on a specific status.
 */
public class SubmissionManager {
    private HashMap<String, Submission> submissions = new HashMap<>();

    /**
     * Checks if a submission with the given ID already exists in the collection.
     *
     * @param submissionId the ID of the submission to check
     * @return true if the submission exists, false otherwise
     */
    public boolean submissionAlreadyExists(String submissionId) { 
        return submissions.containsKey(submissionId); 
    }

    /**
     * Retrieves a submission based on its unique ID.
     *
     * @param submissionId the ID of the submission to retrieve
     * @return the submission object associated with the given ID, or null if not found
     */
    public Submission getSubmission(String submissionId) { 
        return submissions.get(submissionId); 
    }

    /**
     * Adds a submission to the collection, ensuring no duplicate submission IDs.
     * If the submission ID does not already exist, the submission is added.
     *
     * @param submissionPath the path of the submission to be added
     */
    public void addSubmission(String submissionPath) { 
        Submission submission = new Submission(submissionPath);
        if (!submissionAlreadyExists(submission.getSubmissionId())) { 
            submissions.put(submission.getSubmissionId(), submission); 
        }
    }

    /**
     * Adds multiple submissions to the collection. Each submission is added only 
     * if it doesn't already exist in the collection.
     *
     * @param submissionPaths a list of submission paths to be added
     */
    public void bulkAddSubmissions(List<String> submissionPaths) { 
        submissionPaths.forEach(this::addSubmission); 
    }

    /**
     * Prints out the path of all submissions in the collection.
     */
    public void getAllSubmissions() { 
        submissions.forEach((submissionId, submission) -> 
        System.out.println("Submission Path: " + submission.getSubmissionPath()));
    }

    /**
     * Filters submissions based on their status. Returns a map of submission IDs and
     * their corresponding submission objects that match the given status.
     *
     * @param status the status to filter submissions by (e.g., "SUBMITTED", "INVALID")
     * @return a map of submission IDs and submission objects matching the given status
     */
    public Map<String, Submission> filterSubmissionsByStatus(String status) {
        return submissions.entrySet()
                          .stream()
                          .filter(entry -> entry.getValue().getSubmissionStatus().equals(status))
                          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Internal iterator class to traverse submissions based on a specific status filter.
     * This iterator only returns submissions that match the provided status.
     */
    public static class SubmissionIterator implements Iterable<Submission> {
        private final Map<String, Submission> submissions;
        private final String statusFilter;

        /**
         * Constructor for creating an iterator that filters submissions by a given status.
         *
         * @param submissions the map of submissions to iterate over
         * @param statusFilter the status to filter submissions by (e.g., "SUBMITTED")
         */
        public SubmissionIterator(Map<String, Submission> submissions, String statusFilter) {
            this.submissions = submissions;
            this.statusFilter = statusFilter;
        }

        /**
         * Returns an iterator to traverse the submissions filtered by status.
         * The iterator fetches submissions that match the given status filter.
         *
         * @return an iterator for the filtered submissions
         */
        @Override
        public Iterator<Submission> iterator() {
            return new Iterator<Submission>() {
                private final Iterator<Map.Entry<String, Submission>> iterator = submissions.entrySet().iterator();
                private Submission nextMatch;

                /**
                 * Rolls the iterator forward to find the next matching submission based on status.
                 */
                private void roll() {
                    nextMatch = null;
                    while (iterator.hasNext()) {  
                        Map.Entry<String, Submission> entry = iterator.next();
                        if (entry.getValue().getSubmissionStatus().equals(statusFilter)) {
                            nextMatch = entry.getValue();
                            break;
                        }
                    }
                }

                /**
                 * Checks if there are more matching submissions to iterate over.
                 *
                 * @return true if there are more matching submissions, false otherwise
                 */
                @Override
                public boolean hasNext() {
                    if (nextMatch == null) { roll(); }
                    return nextMatch != null;
                }

                /**
                 * Returns the next matching submission in the iteration.
                 *
                 * @return the next matching submission
                 * @throws IllegalStateException if there are no more matching submissions
                 */
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
