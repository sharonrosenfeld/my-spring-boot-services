package com.sharon.dataaggregator.service;

import com.sharon.dataaggregator.DataAggregatorApplication;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

import com.sharon.dataaggregator.config.RabbitMQConfig;
import com.sharon.dataaggregator.model.APIUserData;
import com.sharon.dataaggregator.model.SiteUserData;

@Service
public class DataAggregatorMgr {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAggregatorMgr.class);

    private final static String API_SERVICE = "api";
    private final static String SITE_SERVICE = "service";

    @AllArgsConstructor
    private class APIRunnable implements Runnable{

        private String id;
        private AmqpTemplate amqpTemplate;
        private APIService apiService;

        @Override
        public void run() {
            APIUserData apiUserData = apiService.getUserData(id);
            LOGGER.info("sending {} via rabbitMq",apiUserData);
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.API_DATA_RESPONSE_ROUTINGKEY,apiUserData);
        }
    }

    @AllArgsConstructor
    private class SiteRunnable implements Runnable{

        private String id;
        private AmqpTemplate amqpTemplate;
        private SiteService siteService;

        @Override
        public void run() {
            SiteUserData siteUserData = siteService.getUserData(id);
            LOGGER.info("sending {} via rabbitMq",siteUserData);
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.SITE_DATA_RESPONSE_ROUTINGKEY,siteUserData);
        }
    }

    @Autowired
    AmqpTemplate amqpTemplate;

    //bulkhead
    @Qualifier("APIExecutor")
    @Autowired
    private Executor apiExecutor;

    @Autowired
    private Executor siteExecutor;

    @Autowired
    APIService apiService;

    @Autowired
    SiteService siteService;

    public void deliverData(String id, String serviceName ){
        switch (serviceName){
            case API_SERVICE:{
                   apiExecutor.execute(new APIRunnable(id,amqpTemplate,apiService));
                break;
            }
            case SITE_SERVICE:{
                siteExecutor.execute(new SiteRunnable(id,amqpTemplate,siteService));
                break;
            }
            default:;
        }
    }

}
