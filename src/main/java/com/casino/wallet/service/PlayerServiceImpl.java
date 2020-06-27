package com.casino.wallet.service;

import com.casino.wallet.dto.mapper.PlayerMapper;
import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.exception.EntityType;
import com.casino.wallet.exception.ExceptionType;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.player.Player;
import com.casino.wallet.repository.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.casino.wallet.exception.EntityType.PLAYER;
import static com.casino.wallet.exception.ExceptionType.*;

@Component
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<PlayerDto> getAllPlayers() {
        return StreamSupport.stream(playerRepository.findAll().spliterator(),false)
                .map(PlayerMapper::toPlayerDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDto getPlayerById(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()){
            return PlayerMapper.toPlayerDto(player.get());
        }
        throw exception(PLAYER, ENTITY_NOT_FOUND,id.toString());
    }

    @Override
    public PlayerDto registerPlayer(PlayerDto playerDto) {
        Player player = playerRepository.findByEmail(playerDto.getEmail());
        if (player == null) {
            player = new Player()
                    .setEmail(playerDto.getEmail())
                    .setFirstName(playerDto.getFirstName())
                    .setLastName(playerDto.getLastName());
            return (PlayerMapper.toPlayerDto(playerRepository.save(player)));
        }
        throw exception(PLAYER,DUPLICATE_ENTITY,playerDto.getEmail());
    }

    @Override
    public PlayerDto updatePlayer(PlayerDto playerDto) {
        Optional<Player> oldPlayerById = Optional.ofNullable(playerRepository.findById(playerDto.getId()));
        if(oldPlayerById.isPresent()){
            Player oldPlayerByEmail = playerRepository.findByEmail(playerDto.getEmail());
            if (oldPlayerByEmail == null || oldPlayerByEmail.getId() == playerDto.getId()) {
                oldPlayerByEmail = new Player()
                        .setEmail(playerDto.getEmail())
                        .setFirstName(playerDto.getFirstName())
                        .setId(playerDto.getId())
                        .setCreated(oldPlayerById.get().getCreated())
                        .setLastName(playerDto.getLastName());
                return (PlayerMapper.toPlayerDto(playerRepository.save(oldPlayerByEmail)));
            }
            throw exception(PLAYER,DUPLICATE_ENTITY,playerDto.getEmail());
        }
        throw exception(PLAYER, ENTITY_NOT_FOUND, String.valueOf(playerDto.getId()));
    }

    @Override
    public PlayerDto deletePlayer(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            playerRepository.deleteById(id);
            Optional<Player> deletedPlayer = playerRepository.findById(id);
            if (deletedPlayer.isEmpty()) {
                return PlayerMapper.toPlayerDto(player.get());
            }
            throw exception(PLAYER, DELETION_EXCEPTION,id.toString());
        }
        throw exception(PLAYER, ENTITY_NOT_FOUND,id.toString());
    }

    private RuntimeException exception (EntityType entityType, ExceptionType exceptionType, String... args){
        return WalletException.throwException(entityType,exceptionType,args);
    }
}
