package com.marcotte.api.composite.product;


public class ReviewSummary {
    private final int reviewId;
    private final String author;
    private final String subject;


    public ReviewSummary(int reviewId, String author, String subject) {
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
    }

    public int getReviewId() {
        return this.reviewId;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getSubject() {
        return this.subject;
    }
}
