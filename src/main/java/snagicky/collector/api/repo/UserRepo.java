package snagicky.collector.api.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.User;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
}
