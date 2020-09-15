package com.sharon.dataws.config;

import com.sharon.dataws.repository.InternalPendingRequestsRepository;
import com.sharon.dataws.repository.PendingRequestsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PendingRequestsRepository pendingRequestsRepository()
    {
        return new InternalPendingRequestsRepository();
    }
}
