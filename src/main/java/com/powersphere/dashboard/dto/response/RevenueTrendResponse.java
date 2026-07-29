package com.powersphere.dashboard.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RevenueTrendResponse {

    private List<String> labels;
    private List<BigDecimal> data;
    private String period;

    public RevenueTrendResponse() {
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public RevenueTrendResponse(List<String> labels, List<BigDecimal> data, String period) {
        this.labels = labels;
        this.data = data;
        this.period = period;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
