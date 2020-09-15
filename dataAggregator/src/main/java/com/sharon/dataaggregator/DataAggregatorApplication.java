package com.sharon.dataaggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sharon.dataaggregator.config.RabbitMQConfig;
import com.sharon.dataaggregator.model.UserDataDispatchedRequest;
import com.sharon.dataaggregator.service.DataAggregatorMgr;

@SpringBootApplication
public class DataAggregatorApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataAggregatorApplication.class);

	@Autowired
	DataAggregatorMgr dataAggregatorMgr;

	@RabbitListener(queues = { RabbitMQConfig.USER_DATA_REQUEST_QUEUE })
	public void receiveDataRequest(UserDataDispatchedRequest  message) {
		LOGGER.info("received DATA REQUEST " + message);
		dataAggregatorMgr.deliverData(message.getUserId(), message.getServiceName());
	}

	public static void main(String[] args) {
		SpringApplication.run(DataAggregatorApplication.class, args);
	}
}
