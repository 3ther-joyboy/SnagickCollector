package snagicky.collector.api.repo;

import jakarta.transaction.Transactional;
import org.hibernate.Remove;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.User;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    boolean existsByName(String Name);
    List<User> findByName(String Name);

    @Query(value = "SELECT * FROM user WHERE user.perrmission = 0 AND  UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(user.ctime) > 3600",nativeQuery = true)
    Iterable<User> GetUserForDeletion();
}
