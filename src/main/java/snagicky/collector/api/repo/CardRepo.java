package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snagicky.collector.api.model.Card;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface CardRepo extends CrudRepository<Card,Long> {

    @Query(nativeQuery = true,value = "SELECT " +
            "* " +
//                    "FROM card_edition INNER JOIN card on card.id = card_edition.card " +
            "WHERE " +

            "(:Id is null or card.id = :Id) and " +




            "(:Creator is null or card.created_by = :Creator ) and " +

            "(:Name is null or INSTR(card.name,:Name)) and " +
            "(:Description is null or INSTR(card.description,:Description)) and " +
            "(:Story is null or INSTR(card.story,:Story)) and " +

//                    "(:Edition is null or card_edition.edition_id = :Edition) and " +
            "(:Type is null or card.type = :Type) and " +
            "(:Rarity is null or card.rarity = :Rarity) and " +


            "(:Attack is null or card.attack = :Attack) and " +
            "(:Defense is null or card.defense = :Defense) and " +

            "(CASE :TotalToggle " +
            "WHEN -1 THEN (:TotalCost is null or (card.white + card.green + card.blue + card.red + card.multi) < :TotalCost) " +
            "WHEN 1 THEN (:TotalCost is null or (card.white + card.green + card.blue + card.red + card.multi) >= :TotalCost) " +
            "ELSE (:TotalCost is null or (card.white + card.green + card.blue + card.red + card.multi) = :TotalCost) " +
            "END) and " +
            "(CASE :MultiToggle " +
            "WHEN -1 THEN (:MultiCost is null or card.multi < :MultiCost) " +
            "WHEN 1 THEN (:MultiCost is null or card.multi >= :MultiCost) " +
            "ELSE (:MultiCost is null or card.multi = :MultiCost) " +
            "END) and " +
            "(CASE :WhiteToggle " +
            "WHEN -1 THEN (:WhiteCost is null or card.white < :WhiteCost) " +
            "WHEN 1  THEN (:WhiteCost is null or card.white >= :WhiteCost) " +
            "ELSE (:WhiteCost is null or card.white = :WhiteCost) " +
            "END) and " +
            "(CASE :GreenToggle " +
            "WHEN -1 THEN (:GreenCost is null or card.green < :GreenCost) " +
            "WHEN 1 THEN (:GreenCost is null or card.green >= :GreenCost) " +
            "ELSE (:GreenCost is null or card.green = :GreenCost) " +
            "END) and " +
            "(CASE :BlueToggle " +
            "WHEN -1 THEN (:BlueCost is null or card.blue < :BlueCost) " +
            "WHEN 1 THEN (:BlueCost is null or card.blue >= :BlueCost) " +
            "ELSE (:BlueCost is null or card.blue = :BlueCost) " +
            "END) and " +
            "(CASE :RedToggle " +
            "WHEN -1 THEN (:RedCost is null or card.red < :RedCost) " +
            "WHEN 1 THEN (:RedCost is null or card.red >= :RedCost) " +
            "ELSE (:RedCost is null or card.red = :RedCost) " +
            "END) " +

            "ORDER BY card.name DESC LIMIT :Limit OFFSET :Offset"
    )
    Iterable<Card> FindCard(
            @Param("Id") Integer id,
            @Param("Creator") Integer creator,
            //           @Param("Owner") Intiger owner,

            @Param("Edition") Integer edition,

            @Param("Name") String name,
            @Param("Type") Integer type,
            @Param("Rarity") String rarity,
            @Param("Description") String description,
            @Param("Story") String story,

            @Param("Attack") Integer attack,
            @Param("Defense") Integer defense,

            @Param("MultiCost") Integer multi,
            @Param("TotalCost") Integer cost,
            @Param("WhiteCost") Integer white,
            @Param("GreenCost") Integer green,
            @Param("BlueCost") Integer blue,
            @Param("RedCost") Integer red,

            @Param("MultiToggle") Integer multi_toggle,
            @Param("TotalToggle") Integer cost_toggle,
            @Param("WhiteToggle") Integer white_toggle,
            @Param("GreenToggle") Integer green_toggle,
            @Param("BlueToggle") Integer blue_toggle,
            @Param("RedToggle") Integer red_toggle,

            @Param("Offset") Integer offset,
            @Param("Limit") Integer limit
    );
}
