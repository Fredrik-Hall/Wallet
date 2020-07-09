package com.casino.wallet.repository;

import com.casino.wallet.model.player.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByEmail(String email);
}
