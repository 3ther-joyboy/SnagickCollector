package snagicky.collector.api.repo;

import snagicky.collector.api.model.SubType;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface SubTypeRepo extends CrudRepository<SubType,Long> {
}
