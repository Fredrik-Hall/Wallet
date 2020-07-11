package com.casino.wallet.dto.mapper;

import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.model.player.Player;

public class PlayerMapper {
    public static PlayerDto toPlayerDto(Player player) {
        return new PlayerDto()
                .setCreated(player.getCreated())
                .setEmail(player.getEmail())
                .setFirstName(player.getFirstName())
                .setLastName(player.getLastName())
                .setId(player.getId());
    }
}
