package snagicky.collector.api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
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

    @Column(name = "name",unique = true, nullable = false)
    public String Name;
    @Column(name = "bio")
    public String Bio;

    @Column(name = "password")
    public int Password;

    @Column(name = "re_email", nullable = true)
    public String Email;

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
    // visitor  = 0 (perrmissions as user1 but is deleted after a while, email less)
    // user     = 1 (cant add test cards)
    // User     = 2
    // admin    = 3 (can edit just cards)
    // Admin    = 4 (can manage lvl 1 profiles)
    // Root     = 5 (have to be created through database)

    @UpdateTimestamp
    public Timestamp UTime;
    @CreationTimestamp
    public Timestamp CTime;

    public int Salt(String password) { // [CREATE YOUR OWN SALT](https://www.youtube.com/watch?v=8ZtInClXe1Q)
        // this is place holder
        String StringPassword = Name + password + CTime + "PlaceForEnvironmentVariable";
        return StringPassword.hashCode();
    }
    public boolean CheckPassword(String password){
        return (Password == Salt(password));
    }
}
