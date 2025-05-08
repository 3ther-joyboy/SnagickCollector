package snagicky.collector.api.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.User;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    boolean existsByName(String Name);
    List<User> findByName(String Name);
}
