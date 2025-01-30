package snagicky.collector.api.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name")
    public String Name;
    @Column(name = "bio")
    public String Bio;

    @OneToMany(mappedBy = "ByUser")
    public Set<Card> CreatedCards;

    @ManyToMany()
    @JoinTable(
            name = "card_user",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "card")
    )
    public Set<Card> OwnedCards;

    @ManyToMany()
    @JoinTable(
            name = "card_saved_user",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "card")
    )
    public Set<Card> SavedCards;

    @Column(name = "perrmission")
    public int Perrmission = 0;
    // visitor  = 0
    // user     = 1
    // admin    = 2
    // root     = 3 (have to be created thrue database)
}
