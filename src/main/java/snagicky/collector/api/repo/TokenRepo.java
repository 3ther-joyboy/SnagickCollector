package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Token;

import java.util.UUID;

@Repository
public interface TokenRepo extends CrudRepository<Token,Long> {
    @Query(countQuery = "SELECT * FROM token WHERE code = :Code",nativeQuery = true)
    Token TokenFromUUID(@Param("Code") UUID code);

    @Query(countQuery = "SELECT EXISTs( SELECT * FROM token WHERE code = :Code)",nativeQuery = true)
    long TokenExists(@Param("Code") UUID code);
}
