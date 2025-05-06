package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Token;

import java.util.UUID;

@Repository
public interface TokenRepo extends CrudRepository<Token,UUID> {
    @Query(countQuery = "SELECT EXISTs( SELECT * FROM token WHERE code = :Code)",nativeQuery = true)
    long TokenExists(@Param("Code") UUID code);

    @Query(nativeQuery = true,value = "DELETE FROM token WHERE user=:Id")
    void LogOutAll( @Param("Id") Long id );
}
