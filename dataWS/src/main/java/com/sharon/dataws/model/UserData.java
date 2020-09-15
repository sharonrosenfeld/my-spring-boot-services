package com.sharon.dataws.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class UserData {
    private String id;
    private Timestamp updateDtApi;
    private Integer salaryApi;
    private Timestamp updateDtSite;
    private Integer salarySite;
}
