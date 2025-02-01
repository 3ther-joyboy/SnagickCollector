package snagicky.collector.api.repo;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import snagicky.collector.api.model.Passive;

@Repository
public interface PassiveRepo extends CrudRepository<Passive,Long> {
}
