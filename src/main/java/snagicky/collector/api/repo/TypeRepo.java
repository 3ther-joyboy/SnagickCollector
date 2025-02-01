package snagicky.collector.api.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Type;

@Repository
public interface TypeRepo extends CrudRepository<Type,Long> {
}
