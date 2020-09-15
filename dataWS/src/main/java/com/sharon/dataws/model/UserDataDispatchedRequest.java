package com.sharon.dataws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserDataDispatchedRequest implements Serializable {
    private final String serviceType;
    private final String userId;
    @Override
    public String toString() {
        return "User Data Request [service=" + serviceType + ", usrId=" + userId + "]";
    }

}
