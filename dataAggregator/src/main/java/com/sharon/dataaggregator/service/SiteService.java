package com.sharon.dataaggregator.service;

import com.sharon.dataaggregator.model.SiteUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SiteService  {

    @Autowired
    @Qualifier("siteRestTemplate")
    private RestTemplate restTemplate;

    @Value("${application.siteurl}")
    private String url;

    public SiteUserData getUserData(String id){
        return restTemplate.getForObject(url+"?id="+id, SiteUserData.class);
    }

}