package admin.service;

import admin.entity.Card;

public interface CardService {
    Iterable<Card> findAll();
    
}
