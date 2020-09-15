package com.sharon.dataaggregator.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserDataDispatchedRequest implements Serializable {
    private final String serviceName;
    private final String userId;

    public UserDataDispatchedRequest(@JsonProperty("userId") String id, @JsonProperty("serviceName") String serviceName) {
        this.userId = id;
        this.serviceName = serviceName;
    }


    @Override
    public String toString() {
        return "User Data Request [service=" + serviceName + ", usrId=" + userId + "]";
    }

}
