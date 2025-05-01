package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "type")
public class Type {
    public Type(){}
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name")
    public String Name;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    public Set<Card> Cards;

    @ManyToOne
    @JoinColumn(name = "sub_type",nullable = false)
    public SubType subType;

}
