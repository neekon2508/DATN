package admin.service;

import admin.dto.CardDTO;
import admin.dto.CardSetDTO;


public interface CardSetService {
    Iterable<CardSetDTO> findAll();
    CardSetDTO findCardSetDTOById(Long id);
    CardSetDTO addCardToCardSet(Long id, CardDTO cardDTO);
    CardSetDTO updateCardSet(Long id, CardSetDTO cardSetDTO);
    Iterable<CardSetDTO> getByName(String name);
}
