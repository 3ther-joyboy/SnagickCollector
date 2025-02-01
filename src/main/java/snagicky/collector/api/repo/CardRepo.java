package snagicky.collector.api.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snagicky.collector.api.model.Card;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface CardRepo extends CrudRepository<Card,Long> {

    @Query(nativeQuery = true,value = "SELECT " +
            "* " +
            "FROM card_edition INNER JOIN card on card.id = card_edition.card_id " +
            "WHERE " +
            "(:Id is null or card.id = :Id) and " +

            "(:Name is null or INSTR(card.name,:Name)) and " +
            "(:Type is null or cards.type = :Type) and " +
            "(:Edition is null or card_edition.edition_id = :Edition) and " +

            "(:Rarity is null or cards.rarity = :Rarity) and " +
            "(:Description is null or INSTR(cards.description,:Description)) and " +
            "(:Story is null or INSTR(cards.story,:Story)) and " +

            "(:Attack is null or cards.attack = :Attack) and " +
            "(:Defense is null or cards.defense = :Defense) and " +

            "(CASE :TotalToggle " +
            "WHEN -1 THEN (:TotalCost is null or (cards.white + cards.green + cards.blue + cards.red + cards.multi) < :TotalCost) " +
            "WHEN 1 THEN (:TotalCost is null or (cards.white + cards.green + cards.blue + cards.red + cards.multi) >= :TotalCost) " +
            "ELSE (:TotalCost is null or (cards.white + cards.green + cards.blue + cards.red + cards.multi) = :TotalCost) " +
            "END) and " +
            "(CASE :MultiToggle " +
            "WHEN -1 THEN (:MultiCost is null or cards.multi < :MultiCost) " +
            "WHEN 1 THEN (:MultiCost is null or cards.multi >= :MultiCost) " +
            "ELSE (:MultiCost is null or cards.multi = :MultiCost) " +
            "END) and " +
            "(CASE :WhiteToggle " +
            "WHEN -1 THEN (:WhiteCost is null or cards.white < :WhiteCost) " +
            "WHEN 1  THEN (:WhiteCost is null or cards.white >= :WhiteCost) " +
            "ELSE (:WhiteCost is null or cards.white = :WhiteCost) " +
            "END) and " +
            "(CASE :GreenToggle " +
            "WHEN -1 THEN (:GreenCost is null or cards.green < :GreenCost) " +
            "WHEN 1 THEN (:GreenCost is null or cards.green >= :GreenCost) " +
            "ELSE (:GreenCost is null or cards.green = :GreenCost) " +
            "END) and " +
            "(CASE :BlueToggle " +
            "WHEN -1 THEN (:BlueCost is null or cards.blue < :BlueCost) " +
            "WHEN 1 THEN (:BlueCost is null or cards.blue >= :BlueCost) " +
            "ELSE (:BlueCost is null or cards.blue = :BlueCost) " +
            "END) and " +
            "(CASE :RedToggle " +
            "WHEN -1 THEN (:RedCost is null or cards.red < :RedCost) " +
            "WHEN 1 THEN (:RedCost is null or cards.red >= :RedCost) " +
            "ELSE (:RedCost is null or cards.red = :RedCost) " +
            "END) " +

            "ORDER BY cards.name DESC LIMIT :Limit OFFSET :Offset"
    )
    Iterable<Card> FindCard(
            @Param("Id") Integer id,
            @Param("Edition") Integer edition,
            @Param("Owner") Integer owner,
            @Param("Poaster") Integer poaster,

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
    // i am sorry, i trully dont know how to fix this, ill look for more

}
