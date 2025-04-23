package admin.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import admin.dto.CardSetDTO;

@Service
public class RestCardSetService implements CardSetService{

    private RestTemplate restTemplate;
    
    @Autowired
    public RestCardSetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.cors.origins}")
    private String corsOrigins;

    @Override
    public Iterable<CardSetDTO> findAll() {
       return Arrays.asList(restTemplate.getForObject(corsOrigins+"/api/card_set/get_all", 
       CardSetDTO[].class));
    }

}
