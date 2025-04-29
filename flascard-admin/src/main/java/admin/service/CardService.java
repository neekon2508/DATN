package admin.service;

import admin.dto.CardDTO;
import admin.dto.CardSetDTO;


public interface CardService {
    Iterable<CardDTO> findAll();
   
}
