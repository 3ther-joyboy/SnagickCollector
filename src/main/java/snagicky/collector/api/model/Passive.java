package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "passive")
public class Passive {
    public Passive(){}

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name")
    public String Name;

    @Column(name = "description")
    public String Description;

    @ManyToMany()
    @JoinTable(
            name = "card_passive",
            joinColumns = @JoinColumn(name = "passive"),
            inverseJoinColumns = @JoinColumn(name = "card")
    )
    public Set<Card> Cards;
}