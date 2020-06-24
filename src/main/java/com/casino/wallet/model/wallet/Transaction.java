package com.casino.wallet.model.wallet;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "Transactions")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double amount;
    private int playerId;
    private String transactionId;
    private int direction;
    private Instant timestamp;

    public Transaction(){}

    public Transaction(double amount, int playerId, String transactionId, int direction) {

        this.timestamp = Instant.now();
        this.amount = amount;
        this.playerId = playerId;
        this.transactionId = transactionId;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", playerId=" + playerId +
                ", transactionId='" + transactionId + '\'' +
                ", direction=" + direction +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getDirection() {
        return direction;
    }

    public double getAmount() {
        return amount;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
