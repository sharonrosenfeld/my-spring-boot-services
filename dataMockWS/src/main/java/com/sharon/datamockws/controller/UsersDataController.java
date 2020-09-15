package com.sharon.datamockws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.sharon.datamockws.dto.UserSiteData;
import com.sharon.datamockws.dto.UserAPIData;

@RestController
@RequestMapping(value = "/usersData")
public class UsersDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersDataController.class);

//    @GetMapping(value = "/all")
//    public List<Users> getAll() {
//        return usersDataRepository.findAll();
//    }


    @GetMapping(value = "/apidata")
    public UserAPIData getAPIData(@RequestParam(value = "id") String id) {
        UserAPIData data = new UserAPIData(id,2000);
        return data;
    }

    @GetMapping(value = "/sitedata")
    public UserSiteData getSiteData(@RequestParam(value = "id") String id) {
        UserSiteData data = new UserSiteData(id,3000);
        return data;
    }


//    @PostMapping(value = "/load")
//    public List<Users> persist(@RequestBody final Users users) {
//        usersDataRepository.save(users);
//        return usersDataRepository.findAll();
//    }

}
