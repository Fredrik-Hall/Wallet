package com.casino.wallet.service;

import com.casino.wallet.dto.model.PlayerDto;

import java.util.List;

public interface PlayerService {

    List<PlayerDto> getAllPlayers();

    PlayerDto getPlayerById(Long playerId);

    PlayerDto registerPlayer(PlayerDto playerDto);

    PlayerDto updatePlayer(PlayerDto playerDto);

    PlayerDto deletePlayer(Long playerId);

}
