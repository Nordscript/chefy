package com.nordscript.chefy;

import org.json.JSONArray;

import java.util.Date;

/**
 * Created by Setup on 23/12/2015.
 */
public class Order {
    private Date startTime;
    private Date endTime;
    private String client;
    private String contactNumber;

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getClient() {
        return client;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    private Boolean isCompleted;
    private JSONArray dishes;

    public Order(Date startTime, Date endTime, String client, String contactNumber) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.client = client;
        this.contactNumber = contactNumber;
        this.isCompleted = false;
    }
}
