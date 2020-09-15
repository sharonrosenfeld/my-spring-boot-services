package com.sharon.dataaggregator.service;

import com.sharon.dataaggregator.model.APIUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {

    @Value("${application.apiurl}")
    private String url;

    @Autowired
    @Qualifier("apiRestTemplate")
    private RestTemplate restTemplate;

    public APIUserData getUserData(String id){
         return restTemplate.getForObject(url+"?id="+id,APIUserData.class);
    }

}