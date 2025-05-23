package snagicky.collector.api.repo;

import jakarta.transaction.Transactional;
import org.hibernate.Remove;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.SubType;
import snagicky.collector.api.model.User;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    boolean existsByName(String Name);
    List<User> findByName(String Name);

    @Query(value = "SELECT * FROM user WHERE user.perrmission = 0 AND  UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(user.ctime) > 3600",nativeQuery = true)
    Iterable<User> GetUserForDeletion();


    @Query(nativeQuery = true,value = "SELECT * from user where " +
            "(:Id is null or user.id = :Id ) and " +
            "(:Name is null or INSTR(user.name,:Name)) and " +
            "(:Bio is null or INSTR(user.bio, :Bio)) " +

            "ORDER BY user.name DESC LIMIT :End OFFSET :Start"
    )
    Iterable<User> findThem(
            @Param("Id") Long id,

            @Param("Name") String name,

            @Param("Bio") String bio,

            @Param("Start") Integer satrt,
            @Param("End") Integer end
    );
}
