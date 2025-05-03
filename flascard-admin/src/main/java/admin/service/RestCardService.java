package admin.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import admin.dto.CardDTO;
import admin.dto.CardSetDTO;
import admin.entity.Card;

@Service
public class RestCardService implements CardService{

    private RestTemplate restTemplate;

    @Autowired
    public RestCardService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.cors.origins}")
    private String corsOrigins;

    @Override
    public Iterable<CardDTO> findAll() {

        return Arrays.asList(restTemplate.getForObject(corsOrigins+"/api/card/get_all", 
        CardDTO[].class));
    }

    @Override
    public CardDTO updateCard(Long id, CardDTO cardDTO) {
       return restTemplate.patchForObject(corsOrigins+"/api/card/update{id}", cardDTO, CardDTO.class, id);
    }

    @Override
    public void deleteCard(Long id) {
         restTemplate.delete(corsOrigins+"/api/card/delete/{id}", id);
    }

    @Override
    public Iterable<CardDTO> getByText(String text) {
        return Arrays.asList(restTemplate.getForObject(corsOrigins+"/api/card/getByText/{text}", CardDTO[].class, text));
    }



}
