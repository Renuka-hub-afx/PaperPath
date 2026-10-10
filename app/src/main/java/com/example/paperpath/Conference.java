package com.example.paperpath;

import java.io.Serializable;

public class Conference implements Serializable {
    private String id;
    private String acronym;
    private String fullName;
    private String domainTag;
    private String coreRank;
    private String venue;
    private String confDates;
    private String submissionDeadline;
    private int daysLeft;
    private String notificationDate;
    private String topics;
    private String officialUrl;
    private boolean isBookmarked;

    public Conference(String id, String acronym, String fullName, String domainTag,
                      String coreRank, String venue, String confDates,
                      String submissionDeadline, int daysLeft, String notificationDate,
                      String topics, String officialUrl) {
        this.id = id;
        this.acronym = acronym;
        this.fullName = fullName;
        this.domainTag = domainTag;
        this.coreRank = coreRank;
        this.venue = venue;
        this.confDates = confDates;
        this.submissionDeadline = submissionDeadline;
        this.daysLeft = daysLeft;
        this.notificationDate = notificationDate;
        this.topics = topics;
        this.officialUrl = officialUrl;
        this.isBookmarked = false;
    }

    public String getId() { return id; }
    public String getAcronym() { return acronym; }
    public String getFullName() { return fullName; }
    public String getDomainTag() { return domainTag; }
    public String getCoreRank() { return coreRank; }
    public String getVenue() { return venue; }
    public String getConfDates() { return confDates; }
    public String getSubmissionDeadline() { return submissionDeadline; }
    public int getDaysLeft() { return daysLeft; }
    public String getNotificationDate() { return notificationDate; }
    public String getTopics() { return topics; }
    public String getOfficialUrl() { return officialUrl; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
}
