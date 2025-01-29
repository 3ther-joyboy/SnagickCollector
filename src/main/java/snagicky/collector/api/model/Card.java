package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cards")
public class Card {
    public Card(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "created_by")
    public User ByUser;
    @ManyToMany(mappedBy = "Cards")
    public Set<User> OwnedBy;

    @ManyToMany(mappedBy = "Cards")
    @JsonManagedReference
    public Set<Edition> Editions;

    @ManyToMany(mappedBy = "Cards")
    @JsonManagedReference
    public Set<Passive> Passives;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "type",nullable = false)
    public Type type;

    @Column(name = "name",nullable = false)
    public String Name;
    @Column(name = "description")
    public String Description;
    @Column(name = "story")
    public String Story;

    @Column(name = "red", nullable = false)
    public int Red = 0;
    @Column(name = "blue",nullable = false)
    public int Blue = 0;
    @Column(name = "green",nullable = false)
    public int Green = 0;
    @Column(name = "white",nullable = false)
    public int White = 0;
    @Column(name = "multi",nullable = false)
    public int Multi = 0;
}