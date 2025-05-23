package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public String name;
    @Column(name = "bio")
    public String Bio;

    @JsonIgnore
    @Column(name = "password")
    public String Password;

    @JsonIgnore
    @Column(name = "re_email", nullable = true)
    public String Email;

    @JsonIgnoreProperties("ByUser")
    @OneToMany(mappedBy = "ByUser",cascade = CascadeType.PERSIST)
    public Set<Card> CreatedCards;

    @JsonIgnore
    @OneToMany(
            mappedBy = "User",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE
    )
    public Set<Token> Tokens;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(
            name = "card_user",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "card")
    )
    public Set<Card> OwnedCards;

    @JsonIgnore
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
    // Admin    = 2
    // Root     = 3 (have to be created through database)

    @JsonIgnore
    @UpdateTimestamp
    public Timestamp UTime;
    @JsonIgnore
    @CreationTimestamp
    public Timestamp CTime;

    private String SaltPassword(String password){
        return password + CTime + "PlaceForEnvironmentVariable";
    }
    public String Salt(String password) { // [CREATE YOUR OWN SALT](https://www.youtube.com/watch?v=8ZtInClXe1Q)
        CTime.setNanos(0); // It does this somewhere so password when creating the user is different then loging in after
        String StringPassword = SaltPassword(password);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String out = encoder.encode(StringPassword);
        return out;
    }
    public boolean CheckPassword(String password){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(SaltPassword(password),Password);
    }
}
