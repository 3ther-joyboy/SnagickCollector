package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import snagicky.collector.api.model.Edition;
import snagicky.collector.api.model.Type;

@Repository
public interface EditionRepo extends CrudRepository<Edition,Long> {
    @Query(nativeQuery = true,value = "SELECT * from edition where " +
            "(:Id is null or edition.id = :Id ) and " +
            "(:Name is null or INSTR(edition.name,:Name)) and " +

            "(:Description is null or INSTR(edition.description, :Description)) " +

            "ORDER BY edition.name DESC LIMIT :End OFFSET :Start"
    )
    Iterable<Edition> FindEdition(
            @Param("Id") Long id,

            @Param("Name") String name,
            @Param("Description") String des,

            @Param("Start") Integer start,
            @Param("End") Integer end
    );
}
