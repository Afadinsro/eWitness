package com.adino.ewitness;

/**
 * Created by afadinsro on 12/6/17.
 */

public class Report {
    private ReportCategory category;
    private String date;

    public Report(String date, ReportCategory category) {
        this.date = date;
        this.category = category;

    }
}
