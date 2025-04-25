package com.bajaj.health_challenge;

import java.util.List;

public class WebhookResult {
    private String regNo;
    private List<Integer> outcome;

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public List<Integer> getOutcome() {
        return outcome;
    }

    public void setOutcome(List<Integer> outcome) {
        this.outcome = outcome;
    }
}