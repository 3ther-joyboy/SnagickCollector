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

    @Column(name = "termination_date")
    private java.sql.Timestamp TerminationDate;
    @CreationTimestamp
    private java.sql.Timestamp Created;
}
