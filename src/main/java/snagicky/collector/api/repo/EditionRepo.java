package snagicky.collector.api.repo;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import snagicky.collector.api.model.Edition;

@Repository
public interface EditionRepo extends CrudRepository<Edition,Long> {

}
