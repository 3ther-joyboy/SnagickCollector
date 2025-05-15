package snagicky.collector.api.repo.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.hibernate.engine.jdbc.Size;
import snagicky.collector.api.model.Card;

import java.util.*;
import java.util.regex.Pattern;

public class CardRepoCustomImpl implements CardRepoCustom{
    @PersistenceContext
    private EntityManager entityManager;

    // https://www.bealdung.com/spring-data-jpa-query (implementing custom rep)
    // https://www.baeldung.com/hibernate-criteria-queries (creating queries)
    @Override
    public List<Card> findCardAdvanced(Map<String,String> param){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Card> query = cb.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);
        List<Predicate> predicates = new ArrayList<>();

        int Page = 0;
        int PageSize = 20;
        Order order = cb.asc(root.get("id"));
        Predicate edition = cb.isNotEmpty(root.get("Editions"));

        for (Map.Entry<String,String> i : param.entrySet()){
            switch (i.getKey()) {
                case "sort":
                    order = cb.asc(root.get(i.getValue()));
                    break;
                case "rsort":
                    order = cb.desc(root.get(i.getValue()));
                    break;
                case "page":
                    Page = Integer.parseInt(i.getValue());
                    break;
                case "size":
                    PageSize = Integer.parseInt(i.getValue());
                    break;
                case "type":
                    predicates.add(cb.equal(root.get("type").get("Id"),i.getValue()));
                    break;
                case "edition":
                    System.out.println(i.getValue());
                    if(i.getValue().isEmpty())
                        edition = cb.isEmpty(root.get("Editions"));
                    else
                        edition = cb.equal(root.get("Editions").get("Id"),i.getValue());
                    break;

                default:
                if (Pattern.matches("search_.*", i.getKey())) {
                    predicates.add(cb.like(
                            root.get(ParseAway("search", i.getKey())),
                            "%" + i.getValue() + "%"
                    ));
                } else if (Pattern.matches("equals_.*", i.getKey())) {
                    predicates.add(cb.equal(
                            root.get(ParseAway("equals", i.getKey())),
                            i.getValue()
                    ));
                } else if (Pattern.matches("higher_.*", i.getKey())) {
                    predicates.add(cb.greaterThan(
                            root.get(ParseAway("higher", i.getKey())),
                            i.getValue()
                    ));
                } else if (Pattern.matches("lower_.*", i.getKey())) {
                    predicates.add(cb.lessThan(
                            root.get(ParseAway("lower", i.getKey())).get("Id"),
                            i.getValue()
                    ));
                } else if (Pattern.matches("not_.*", i.getKey())) {
                    predicates.add(cb.notEqual(
                            root.get(ParseAway("not", i.getKey())),
                            i.getValue()
                    ));
                } else if (Pattern.matches("link_.*", i.getKey())) {
                    predicates.add(cb.notEqual(
                            root.get(ParseAway("link", i.getKey())).get("Id"),
                            Long.parseLong(i.getValue())-1 // WHAT
                    ));
                }
            }
        }
        predicates.add(edition); // default query is not to search for edition less cards (created bz not admins)

        // To array bs
        Predicate[] whereQuer = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++)
           whereQuer[i] = predicates.get(i);

        query.select(root).where(whereQuer).orderBy(order);
        return entityManager.createQuery(query).setFirstResult(Page * PageSize).setMaxResults(PageSize).getResultList();
    }
    private String ParseAway(String word, String base){
        String out = (String) base.subSequence(word.length()+1,base.length());
        return out;
    }

}
