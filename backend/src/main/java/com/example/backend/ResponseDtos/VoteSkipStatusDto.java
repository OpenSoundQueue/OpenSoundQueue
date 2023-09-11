package com.example.backend.ResponseDtos;

public class VoteSkipStatusDto {
    boolean hasVoted;
    int received;
    int required;

    public VoteSkipStatusDto(boolean hasVoted, int received, int required) {
        this.hasVoted = false;
        this.received = received;
        this.required = required;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public int getReceived() {
        return received;
    }

    public int getRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "VoteSkipStatusDto{" +
                "hasVoted=" + hasVoted +
                ", received=" + received +
                ", required=" + required +
                '}';
    }
}
