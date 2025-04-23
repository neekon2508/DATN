package admin.service;

import admin.dto.CardSetDTO;


public interface CardSetService {
    Iterable<CardSetDTO> findAll();
}
