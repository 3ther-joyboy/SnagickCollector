package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.UUID;

@Entity
@Table(name = "token")
public class Token {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "code")
    public UUID Code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user",nullable = false)
    public User User;

    @JsonIgnore
    @Column(name = "verify_self")
    public boolean VerifySelf = false;
    @JsonIgnore
    @Column(name = "verify_other")
    public boolean VerifyOther = false;
    @JsonIgnore
    @Column(name = "edit_self")
    public boolean EditSelf = false;
    @JsonIgnore
    @Column(name = "delete_self")
    public boolean DeleteSelf = false;
    @JsonIgnore
    @Column(name = "edit_lower")
    public boolean EditLower = false;
    @JsonIgnore
    @Column(name = "delete_lower")
    public boolean DeleteLower = false;
    @JsonIgnore
    @Column(name = "create_cards")
    public boolean CreateCards = false;
    @JsonIgnore
    @Column(name = "create_test_cards")
    public boolean CreateTestCards = false;
    @JsonIgnore
    @Column(name = "edit_cards")
    public boolean EditCards = false;
    @JsonIgnore
    @Column(name = "change_permission")
    public boolean ChangePermission = false;
    @JsonIgnore
    @Column(name = "change_password")
    public boolean ChangePassword = false;

    @Column(name = "termination_date")
    private java.sql.Timestamp TerminationDate;
    @CreationTimestamp
    private java.sql.Timestamp Created;
}
