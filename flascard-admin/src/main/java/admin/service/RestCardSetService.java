package admin.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import admin.dto.CardDTO;
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
    @Override
    public CardSetDTO findCardSetDTOById(Long id) {
      return restTemplate.getForObject(corsOrigins+"/api/card_set/getById/{id}", CardSetDTO.class, id);
    }
    @Override
    public CardSetDTO addCardToCardSet(Long id, CardDTO cardDTO) {
       return restTemplate.postForObject(corsOrigins+"/api/card_set/create_card/{id}", cardDTO,CardSetDTO.class, id);
    }
    @Override
    public CardSetDTO updateCardSet(Long id, CardSetDTO cardSetDTO) {
     return restTemplate.patchForObject(corsOrigins+"/api/card_set/update/{id}", cardSetDTO, CardSetDTO.class, id);
    }
}
