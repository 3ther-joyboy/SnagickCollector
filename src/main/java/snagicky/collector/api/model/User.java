package snagicky.collector.api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name",unique = true)
    public String Name;
    @Column(name = "bio")
    public String Bio;

    @Column(name = "password")
    private int Password;

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

    @UpdateTimestamp
    public Timestamp Time;

    public int Salt(String password) { // [CREATE YOUR OWN SALT](https://www.youtube.com/watch?v=8ZtInClXe1Q)
        // this is place holder
        String StringPassword = Name + password + Time + "PlaceForEnvironmentVariable";
        return StringPassword.hashCode();
    }
    public boolean CheckPassword(String password){
        return (Password == Salt(password));
    }
}
