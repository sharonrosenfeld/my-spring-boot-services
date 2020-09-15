package com.sharon.dataaggregator.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SiteUserData implements Serializable {
    private String id;
    private Integer salary;

    public SiteUserData(@JsonProperty("id") String id, @JsonProperty("salary") Integer salary) {
        this.id = id;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "SiteUserData{" +
                "id='" + id + "'" +
                ", salary=" + salary + "}";
    }
}
