package admin.repository;

import org.springframework.data.repository.CrudRepository;

import admin.entity.Card;

public interface CardRepository extends CrudRepository<Card, Long>{
    
}
