package admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import admin.entity.CardSet;

public interface CardSetRepository extends CrudRepository<CardSet, Long>{

    @EntityGraph(attributePaths = {"cards"})
    Optional<CardSet> findById(Long id);
}
