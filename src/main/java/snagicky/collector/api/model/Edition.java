package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "edition")
public class Edition {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name")
    public String Name;

    @Column(name = "description")
    public String Description;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(
            name = "card_edition",
            joinColumns = @JoinColumn(name = "edition"),
            inverseJoinColumns = @JoinColumn(name = "card")
    )
    public Set<Card> Cards;
}
