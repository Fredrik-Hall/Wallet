package com.casino.wallet.service;

import com.casino.wallet.dto.mapper.PlayerMapper;
import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.exception.EntityType;
import com.casino.wallet.exception.ExceptionType;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.model.player.Player;
import com.casino.wallet.repository.AccountRepository;
import com.casino.wallet.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.casino.wallet.exception.EntityType.ACCOUNT;
import static com.casino.wallet.exception.EntityType.PLAYER;
import static com.casino.wallet.exception.ExceptionType.*;

@Component
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AccountRepository accountRepository;

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
            PlayerDto savedPlayer = PlayerMapper.toPlayerDto(playerRepository.save(player));
            accountRepository.save(new Account()
                    .setId(savedPlayer.getId())
                    .setAmount(0.0));

            return savedPlayer;
        }
        throw exception(PLAYER,DUPLICATE_ENTITY,playerDto.getEmail());
    }

    @Override
    public PlayerDto updatePlayer(PlayerDto playerDto) {
        Optional<Player> playerById = playerRepository.findById(playerDto.getId());
        if(playerById.isPresent()){
            //Make sure the email sent in is not in use by another player
            Player playerByEmail = playerRepository.findByEmail(playerDto.getEmail());
            if (playerByEmail == null || playerByEmail.getId() == playerDto.getId()) {
                Player updatedPlayer = new Player()
                        .setEmail(playerDto.getEmail())
                        .setFirstName(playerDto.getFirstName())
                        .setId(playerDto.getId())
                        .setCreated(playerById.get().getCreated())
                        .setLastName(playerDto.getLastName());
                return PlayerMapper.toPlayerDto(playerRepository.save(updatedPlayer));
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
            accountRepository.deleteById(id);
            Optional<Player> deletedPlayer = playerRepository.findById(id);
            Optional<Account> deletedAccount = accountRepository.findById(id);
            if (deletedPlayer.isEmpty()) {
                if(deletedAccount.isEmpty()){
                    return PlayerMapper.toPlayerDto(player.get());
                }
                throw exception(ACCOUNT, INTERNAL_EXCEPTION,"There was an issue deleting account with id "+id.toString());
            }
            throw exception(PLAYER, INTERNAL_EXCEPTION,"There was an issue deleting player with id "+id.toString());
        }
        throw exception(PLAYER, ENTITY_NOT_FOUND,id.toString());
    }

    private RuntimeException exception (EntityType entityType, ExceptionType exceptionType, String... args){
        return WalletException.throwException(entityType,exceptionType,args);
    }
}
