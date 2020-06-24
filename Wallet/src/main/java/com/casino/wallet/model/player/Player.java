package com.casino.wallet.model.player;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table (name = "Players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiParam(hidden = true)
    private long playerId;
    private String name;
    @ApiParam(hidden = true)
    private Instant created = Instant.now();


    public Player(){}

    public Player(String name){

        this.name = name;
    }


    public Instant getCreated() {
        return created;
    }

    public long getplayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", created=" + created +
                '}';
    }
}
