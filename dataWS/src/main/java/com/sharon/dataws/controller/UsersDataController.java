package com.sharon.dataws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;


import com.sharon.dataws.config.ApplicationProperties;
import com.sharon.dataws.service.UserDataType;
import com.sharon.dataws.dao.UserDataDAO;
import com.sharon.dataws.model.UserData;
import com.sharon.dataws.service.UserDataRefresher;
import com.sharon.dataws.dto.UserDataDto;

@RestController
@RequestMapping(value = "/usersData")
public class UsersDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersDataController.class);

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    UserDataDAO userDataDAO;

    @Autowired
    UserDataRefresher userDataRefresher;

    @Autowired
    private ModelMapper modelMapper;


//    @GetMapping(value = "/all")
//    public List<Users> getAll() {
//        return usersDataRepository.findAll();
//    }

    private void refreshDataIfNeeded(String id, UserData userData){
        boolean refreshAPI = false, refreshSite = false;
        if (userData == null){
            refreshAPI = true; refreshSite = true;
        }else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime apiRefreshTime = now.minusHours(applicationProperties.getApiUserDataExpiration());
            if (userData.getUpdateDtApi().toLocalDateTime().isBefore(apiRefreshTime) ){
                refreshAPI = true;
            }
            LocalDateTime siteRefreshTime = now.minusHours(applicationProperties.getSiteUserDataExpiration());
            if (userData.getUpdateDtSite().toLocalDateTime().isBefore(siteRefreshTime)){
                refreshSite = true;
            }
            LOGGER.info("NOW={} api_last_update={} site_last_update={}",now,userData.getUpdateDtApi(),userData.getUpdateDtSite());
        }

        LOGGER.info("refresh api data {} refresh site data {}",refreshAPI,refreshSite);

        try{
            if (refreshAPI){
                userDataRefresher.refresh(id, UserDataType.API);
            }
            if (refreshSite){
                userDataRefresher.refresh(id, UserDataType.SITE);
            }
        }catch (AmqpException ex){
            LOGGER.error("error refresh API {} Site {} : {}",refreshAPI, refreshSite, ex.getMessage());
        }

    }

    @GetMapping(value = "/user")
    public UserDataDto getUserData(@RequestParam(value = "id") String id) {
        UserData curData = null;
        try{
            curData =  userDataDAO.getUserData(id);
            refreshDataIfNeeded(id,curData);
        }catch (SQLException ex){
            LOGGER.error("failed to get user data id={}. ex={}",id,ex.getMessage());
        }
        UserDataDto userDataDto = null;
        if (curData != null){
            userDataDto = convertToDto(curData);
        }

        return userDataDto;
    }

    private UserDataDto convertToDto(UserData userData) {
        UserDataDto userDataDto = modelMapper.map(userData, UserDataDto.class);
        return userDataDto;
    }



//    @PostMapping(value = "/load")
//    public List<Users> persist(@RequestBody final Users users) {
//        usersDataRepository.save(users);
//        return usersDataRepository.findAll();
//    }

}
