package de.zorcic.kicker.players.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Players.findAll", query = "SELECT p from Player as p ORDER BY p.name ASC")
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    public Player(String name) {
        this.name = name;
    }

    Player() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
