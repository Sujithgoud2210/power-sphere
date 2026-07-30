package com.powersphere.dashboard.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ConsumptionTrendResponse {

    private List<String> labels;
    private List<Double> data;
    private String period;

    public ConsumptionTrendResponse() {
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public ConsumptionTrendResponse(List<String> labels, List<Double> data, String period) {
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

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
