package snagicky.collector.api.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;

import java.util.UUID;

@Repository
public interface TokenRepo extends CrudRepository<Token,UUID> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM token WHERE token.user=:Id",nativeQuery = true)
    void LogOutAll( @Param("Id") Long id );
}
