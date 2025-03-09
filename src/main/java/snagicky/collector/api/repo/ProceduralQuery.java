package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import snagicky.collector.api.model.Card;

public interface ProceduralQuery extends JpaRepository<Card, Long> {
}
