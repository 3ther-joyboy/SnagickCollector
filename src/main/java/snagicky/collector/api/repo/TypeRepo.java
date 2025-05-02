package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import snagicky.collector.api.model.Type;

@Repository
public interface TypeRepo extends CrudRepository<Type,Long> {

    @Query(nativeQuery = true,value = "SELECT * from type where " +
            "(:Id is null or type.id = :Id ) and " +
            "(:Name is null or INSTR(type.name,:Name)) and " +
            "(:SubType is null or type.sub_type = :SubType) and " +

            "ORDER BY type.name DESC LIMIT :End OFFSET :Start"
    )
    Iterable<Type> FindType(
            @Param("Id") Long id,

            @Param("Name") String name,

            @Param("SubType") Long subType,

            @Param("Start") Integer start,
            @Param("End") Integer end
    );
}
