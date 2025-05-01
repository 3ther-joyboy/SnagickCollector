package snagicky.collector.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "sub_type")
public class SubType {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long Id;

    @Column(name = "name")
    public String Name;
    @Column(name = "description")
    public String Description;

    @JsonIgnore
    @OneToMany(mappedBy = "subType")
    public Set<Type> Types;
}
