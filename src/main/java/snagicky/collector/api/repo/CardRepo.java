package snagicky.collector.api.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snagicky.collector.api.model.Card;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import snagicky.collector.api.repo.custom.CardRepoCustom;

import java.util.List;

@Repository
public interface CardRepo extends CrudRepository<Card,Long>, CardRepoCustom {
}
