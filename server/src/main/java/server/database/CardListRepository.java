package server.database;

import commons.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Long> {
}
