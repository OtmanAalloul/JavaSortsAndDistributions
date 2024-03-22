package com.example.projectdatastructure.helpers;

public class item{
    private final Integer id;
    private final Double data;

    public item(Integer id, Double data) {
        this.id = id;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public Double getData() {
        return data;
    }
}