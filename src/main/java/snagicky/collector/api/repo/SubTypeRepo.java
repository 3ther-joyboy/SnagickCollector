package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snagicky.collector.api.model.SubType;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import snagicky.collector.api.model.Type;

@Repository
public interface SubTypeRepo extends CrudRepository<SubType,Long> {

    @Query(nativeQuery = true,value = "SELECT * from sub_type where " +
            "(:Id is null or sub_type.id = :Id ) and " +
            "(:Name is null or INSTR(sub_type.name,:Name)) and " +
            "(:Description is null or INSTR(sub_type.description, :Description)) " +

            "ORDER BY sub_type.name DESC LIMIT :End OFFSET :Start"
    )
    Iterable<SubType> FindSubType(
            @Param("Id") Long id,

            @Param("Name") String name,

            @Param("Description") String des,

            @Param("Start") Integer satrt,
            @Param("End") Integer end
    );
}
