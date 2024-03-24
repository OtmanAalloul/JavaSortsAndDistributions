package com.example.projectdatastructure.helpers;

public class DataInfo {
    private Double[] data;
    private long generationTime;
    private long sortingTime;

    public DataInfo(Double[] data, long generationTime, long sortingTime) {
        this.data = data;
        this.generationTime = generationTime;
        this.sortingTime = sortingTime;
    }

    public Double[] getData() {
        return data;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public long getSortingTime() {
        return sortingTime;
    }

    public void setData(Double[] data) {
        this.data = data;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public void setSortingTime(long sortingTime) {
        this.sortingTime = sortingTime;
    }
}
