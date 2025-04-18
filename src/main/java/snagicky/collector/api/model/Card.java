package snagicky.collector.api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "card")
public class Card {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;


    @CreationTimestamp
    private java.sql.Timestamp Created;
    @UpdateTimestamp
    private java.sql.Timestamp Updated;

    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    public User ByUser;

    @ManyToMany(mappedBy = "OwnedCards")
    public Set<User> OwnedBy;
    @ManyToMany(mappedBy = "SavedCards")
    public Set<User> SavedBy;

    @ManyToMany(mappedBy = "Cards")
    public Set<Edition> Editions;

    @ManyToOne
    @JoinColumn(name = "type",nullable = false)
    public Type type;
    @Column(name = "rarity",nullable = false)
    public String Rarity;

    @Column(name = "attack")
    public int Attack;
    @Column(name = "defense")
    public int Defense;

    @Column(name = "name",nullable = false)
    public String Name;
    @Column(name = "description")
    public String Description;
    @Column(name = "story")
    public String Story;
    @Column(name = "note")
    public String Note;

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
