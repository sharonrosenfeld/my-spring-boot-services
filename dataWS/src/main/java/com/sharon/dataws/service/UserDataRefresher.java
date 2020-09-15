package com.sharon.dataws.service;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.sharon.dataws.config.RabbitMQConfig;
import com.sharon.dataws.dao.UserDataDAO;
import com.sharon.dataws.model.APIUserData;
import com.sharon.dataws.model.SiteUserData;
import com.sharon.dataws.model.UserDataDispatchedRequest;
import com.sharon.dataws.repository.PendingRequestsRepository;


@Service
public class UserDataRefresher {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataRefresher.class);

    private static final Long REQUEST_EXPIRATION_MS = TimeUnit.MINUTES.toMillis(10);

    @Autowired
    private UserDataDAO userDataDAO;

    @Autowired
    private PendingRequestsRepository pendingRequestsRepository;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private String requestKey(String userId, String dataServiceType){
        return userId+"-"+ dataServiceType;
    }

    public void refresh(String userId, UserDataType dataServiceType ) throws AmqpException {
        LOGGER.info("refreshing {} for {} Data",userId,dataServiceType);
        String k = requestKey(userId,dataServiceType.name());
        Long requestEpochTime = pendingRequestsRepository.get(k);
        Long now =  new Date().getTime();
        if ( requestEpochTime != null && requestEpochTime > new Date().getTime() - REQUEST_EXPIRATION_MS){
            LOGGER.info("exist pending data from {}, now={} discarding.. ",requestEpochTime, now);
            return;
        }
        UserDataDispatchedRequest userDataDispatchedRequest = new UserDataDispatchedRequest(userId,dataServiceType.name());
        LOGGER.info("sending {} via rabbitmq",userDataDispatchedRequest);
        rabbitTemplate.convertSendAndReceive(RabbitMQConfig.EXCHANGE,RabbitMQConfig.USER_DATA_REQUEST_ROUTINGKEY, userDataDispatchedRequest);
        pendingRequestsRepository.put(k, new Date().getTime());
    }

    @RabbitListener(queues = RabbitMQConfig.API_DATA_RESPONSE_QUEUE)
    public void receiveMessage(final APIUserData customMessage) {
        LOGGER.info("Received message as specific class: {}", customMessage);
        try {
            userDataDAO.upsertUserAPIData(customMessage);
        }catch (SQLException exception) {
            LOGGER.error("failed to update " + customMessage);
        }
        pendingRequestsRepository.remove(requestKey(customMessage.getId(),UserDataType.API.name()));
    }

    @RabbitListener(queues = RabbitMQConfig.SITE_DATA_RESPONSE_QUEUE)
    public void receiveMessage(final SiteUserData customMessage) {
        LOGGER.info("Received message as specific class: {}", customMessage);
        try {
            userDataDAO.upsertUserSiteData(customMessage);
        }catch (SQLException exception) {
            LOGGER.error("failed to update " + customMessage);
        }
        pendingRequestsRepository.remove(requestKey(customMessage.getId(),UserDataType.SITE.name()));
    }
}
