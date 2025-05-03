package admin.service;

import admin.dto.CardDTO;
import admin.dto.CardSetDTO;


public interface CardService {
    Iterable<CardDTO> findAll();
    CardDTO updateCard(Long id, CardDTO cardDTO);
    void deleteCard(Long id);
    Iterable<CardDTO>  getByText(String text);
}
