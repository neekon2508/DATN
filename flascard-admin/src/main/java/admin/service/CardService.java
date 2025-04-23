package admin.service;

import admin.dto.CardDTO;


public interface CardService {
    Iterable<CardDTO> findAll();
    
}
