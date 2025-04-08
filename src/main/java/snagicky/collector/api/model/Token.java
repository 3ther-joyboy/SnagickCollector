package snagicky.collector.api.model;

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

    @ManyToOne
    @JoinColumn(name = "user",nullable = false)
    public User User;

    @Column(name = "verify_self")
    public boolean VerifySelf = false;
    @Column(name = "verify_other")
    public boolean VerifyOther = false;
    @Column(name = "edit_self")
    public boolean EditSelf = false;
    @Column(name = "delete_self")
    public boolean DeleteSelf = false;
    @Column(name = "edit_lower")
    public boolean EditLower = false;
    @Column(name = "delete_lower")
    public boolean DeleteLower = false;
    @Column(name = "create_cards")
    public boolean CreateCards = false;
    @Column(name = "create_test_cards")
    public boolean CreateTestCards = false;
    @Column(name = "edit_cards")
    public boolean EditCards = false;
    @Column(name = "change_permission")
    public boolean ChangePermission = false;

    @Column(name = "termination_date")
    private java.sql.Timestamp TerminationDate;
    @CreationTimestamp
    private java.sql.Timestamp Created;
}
