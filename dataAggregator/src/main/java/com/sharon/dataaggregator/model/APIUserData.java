package com.sharon.dataaggregator.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class APIUserData implements Serializable {
    private String id;
    private Integer salary;

    public APIUserData(@JsonProperty("id") String id, @JsonProperty("salary") Integer salary) {
        this.id = id;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "APIUserData{" +
                "id='" + id + "'" +
        ", salary=" + salary + "}";
    }
}
