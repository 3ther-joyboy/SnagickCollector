package snagicky.collector.api.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Token;

@Repository
public interface TokenRepo extends CrudRepository<Token,Long> {
}
