package com.casino.wallet.service;

import com.casino.wallet.config.ExceptionConfig;
import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.model.player.Player;
import com.casino.wallet.repository.AccountRepository;
import com.casino.wallet.repository.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerServiceImplTest {

    @TestConfiguration
    static class PlayerServiceImplTestContextConfiguration{

        @Bean(name="mockPlayerService")
        @Primary
        public PlayerService playerService(){
            return new PlayerServiceImpl();
        }

        @Bean(name="mockWalletException")
        public WalletException walletException(){
            return new WalletException(exceptionConfig());
        }

        @Bean(name="mockExceptionConfig")
        public ExceptionConfig exceptionConfig(){
            return Mockito.mock(ExceptionConfig.class);
        }
    }

    @Autowired
    private PlayerService playerService;

    @MockBean
    private ExceptionConfig exceptionConfig;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PlayerRepository playerRepository;

    private final Player player = new Player().setFirstName("Bruce").setLastName("Wayne").setEmail("Bruce@wayneenterprises.com").setId(1).setCreated(Instant.now());
    private final Player playerTwo = new Player().setFirstName("Clark").setLastName("Kent").setEmail("Clarke.Kent@dailyplanet.com").setId(2).setCreated(Instant.now());
    private final Player playerThree = new Player().setFirstName("Diana").setLastName("Prince").setEmail("Diana1245@gmail.com").setId(3).setCreated(Instant.now());
    private final Player playerFour = new Player().setFirstName("Barry").setLastName("Allen").setEmail("Barry.Allen@ccpd.gov").setId(4).setCreated(Instant.now());
    private final Player playerFive = new Player().setFirstName("Hal").setLastName("Jordan").setEmail("Jordan.Hal@us.af.mil").setId(5).setCreated(Instant.now());
    private final Player playerSix = new Player().setFirstName("Arthur").setLastName("Curry").setEmail("Arthur.C@live.com").setId(6).setCreated(Instant.now());
    private final Player playerSeven = new Player().setFirstName("Victor").setLastName("Stone").setEmail("VicSto@metropolisHS.edu").setId(7).setCreated(Instant.now());
    private final List<Player> playerList = Arrays.asList(player,
            playerTwo,playerThree,playerFour,
            playerFive,playerSix,playerSeven);
    private Account playerAccount = new Account().setId(1).setAmount(0);

    @Before
    public void setUp(){

        Optional<Account> emptyAccount = Optional.empty();
        Optional<Player> emptyPlayer = Optional.empty();

        Mockito.when(playerRepository.findAll())
                .thenReturn(playerList);

        Mockito.when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));
        Mockito.when(playerRepository.findById(playerTwo.getId()))
                .thenReturn(Optional.of(playerTwo));
        Mockito.when(playerRepository.findById(playerThree.getId()))
                .thenReturn(Optional.of(playerThree));
        Mockito.when(playerRepository.findById(playerFour.getId()))
                .thenReturn(Optional.of(playerFour)).thenReturn(emptyPlayer);
        Mockito.when(playerRepository.findById(playerFive.getId()))
                .thenReturn(Optional.of(playerFive));
        Mockito.when(playerRepository.findById(playerSix.getId()))
                .thenReturn(Optional.of(playerSix)).thenReturn(emptyPlayer);
        Mockito.when(playerRepository.findById(playerSeven.getId()))
                .thenReturn(Optional.of(playerSeven));

        Mockito.when(playerRepository.findByEmail(player.getEmail()))
                .thenReturn(null);
        Mockito.when(playerRepository.findByEmail(playerTwo.getEmail()))
                .thenReturn(playerTwo);
        Mockito.when(playerRepository.findByEmail(playerThree.getEmail()))
                .thenReturn(playerThree);

        Mockito.when(accountRepository.findById(playerFour.getId()))
                .thenReturn(emptyAccount);
        Mockito.when(accountRepository.findById(playerSix.getId()))
                .thenReturn(Optional.ofNullable(playerAccount));

        String playerNotFoundException = "player.not.found";
        String playerDuplicateException = "player.duplicate";
        String playerInternalException = "player.internal";
        String accountInternalException = "account.internal";

        Mockito.when(exceptionConfig.getConfigValue(playerInternalException))
                .thenReturn("There was an issue deleting player with id");
        Mockito.when(exceptionConfig.getConfigValue(accountInternalException))
                .thenReturn("There was an issue deleting account with id");
        Mockito.when(exceptionConfig.getConfigValue(playerNotFoundException))
                .thenReturn("Player with id {0} was not found.");
        Mockito.when(exceptionConfig.getConfigValue(playerDuplicateException))
                .thenReturn("A player with the email address {0} already exists.");
    }

    @Test
    public void getAllPlayers_whenCalled_thenAListOfPlayersShouldBeReturned(){
        //Given
        String expectedFirstName = player.getFirstName();
        String expectedLastName = player.getLastName();
        String expectedEmail = player.getEmail();
        long expectedId = player.getId();
        int expectedPlayerCount = playerList.size();

        //When
        List<PlayerDto> playerDtos = playerService.getAllPlayers();
        PlayerDto playerOne = playerDtos.get(0);

        //Then
        assertThat(playerDtos.size())
                .isEqualTo(expectedPlayerCount);
        assertThat(playerOne.getFirstName())
                .isEqualTo(expectedFirstName);
        assertThat(playerOne.getLastName())
                .isEqualTo(expectedLastName);
        assertThat(playerOne.getEmail())
                .isEqualTo(expectedEmail);
        assertThat(playerOne.getId())
                .isEqualTo(expectedId);
    }

    @Test
    public void getPlayerById_whenValidId_thenPlayerShouldBeFound(){
        //Given
        String expectedFirstName = player.getFirstName();
        String expectedLastName = player.getLastName();
        String expectedEmail = player.getEmail();
        long expectedId = player.getId();

        //When
        PlayerDto playerOne = playerService.getPlayerById(player.getId());

        //Then
        assertThat(playerOne.getFirstName())
                .isEqualTo(expectedFirstName);
        assertThat(playerOne.getLastName())
                .isEqualTo(expectedLastName);
        assertThat(playerOne.getEmail())
                .isEqualTo(expectedEmail);
        assertThat(playerOne.getId())
                .isEqualTo(expectedId);
    }

    @Test
    public void getPlayerById_whenInvalidId_thenExceptionShouldBeThrown(){
        //Given
        long playerId = 8;
        String expectedMessagePart1 = "Player with id";
        String expectedMessagePart2 =  "was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> playerService.getPlayerById(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));
    }

    @Test
    public void registerPlayer_whenNewPlayerIsRegistered_thenShouldReturnNewPlayer(){
        //Given
        PlayerDto newPlayer = new PlayerDto()
                .setEmail(player.getEmail())
                .setFirstName(player.getFirstName())
                .setLastName(player.getLastName());
        Mockito.when(playerRepository.save(any(Player.class)))
                .thenReturn(player);
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerAccount);

        //When
        PlayerDto registeredPlayer = playerService.registerPlayer(newPlayer);

        //Then
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(registeredPlayer.getFirstName().equals(newPlayer.getFirstName()));
        assertThat(registeredPlayer.getLastName().equals(newPlayer.getLastName()));
        assertThat(registeredPlayer.getEmail().equals(newPlayer.getEmail()));
        assertThat(registeredPlayer.getId()).isEqualTo(player.getId());
    }

    @Test
    public void registerPlayer_whenEmailIsAlreadyInUse_thenShouldThrowException(){
        //Given
        PlayerDto newPlayer = new PlayerDto()
                .setEmail(playerTwo.getEmail())
                .setFirstName(playerTwo.getFirstName())
                .setLastName(playerTwo.getLastName());
        String expectedMessagePart1 = "A player with the email address";
        String expectedMessagePart2 =  "already exists.";

        //When
        Exception exception = assertThrows(WalletException.DuplicateEntityException.class, () -> playerService.registerPlayer(newPlayer));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));

    }

    @Test
    public void updatePlayer_whenPlayerIsCorrectlyUpdated_thenShouldReturnUpdatedPlayer(){
        //Given
        PlayerDto playerDto = new PlayerDto()
                .setId(player.getId())
                .setFirstName(player.getFirstName())
                .setLastName(player.getLastName())
                .setEmail("Batman@live.com");
        Player updatedPlayerOne = player.setEmail("Batman@live.com");

        Mockito.when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayerOne);

        //When
        PlayerDto updatedPlayer = playerService.updatePlayer(playerDto);

        //Then
        assertThat(updatedPlayer.getId())
                .isEqualTo(player.getId());
        assertThat(updatedPlayer.getEmail())
                .isEqualTo(playerDto.getEmail());
        assertThat(updatedPlayer.getFirstName())
                .isEqualTo(player.getFirstName());
        assertThat(updatedPlayer.getLastName())
                .isEqualTo(player.getLastName());
    }
    @Test
    public void updatePlayer_whenNewEmailIsNotChanged_thenShouldReturnUpdatedPlayer(){
        //Given
        PlayerDto playerDto = new PlayerDto()
                .setId(playerTwo.getId())
                .setFirstName(playerTwo.getFirstName())
                .setLastName(playerTwo.getLastName())
                .setEmail(playerTwo.getEmail());

        Mockito.when(playerRepository.save(any(Player.class))).thenReturn(playerTwo);

        //When
        PlayerDto updatedPlayer = playerService.updatePlayer(playerDto);

        //Then
        assertThat(updatedPlayer.getId())
                .isEqualTo(playerTwo.getId());
        assertThat(updatedPlayer.getEmail())
                .isEqualTo(playerTwo.getEmail());
        assertThat(updatedPlayer.getFirstName())
                .isEqualTo(playerTwo.getFirstName());
        assertThat(updatedPlayer.getLastName())
                .isEqualTo(playerTwo.getLastName());
    }
    @Test
    public void updatePlayer_whenPlayerDoesntExist_thenShouldThrowException(){
        //Given
        PlayerDto playerDto = new PlayerDto()
                .setId(8)
                .setFirstName(playerTwo.getFirstName())
                .setLastName(playerTwo.getLastName())
                .setEmail(playerTwo.getEmail());
        String expectedMessagePart1 = "Player with id";
        String expectedMessagePart2 =  "was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> playerService.updatePlayer(playerDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));
    }
    @Test
    public void updatePlayer_whenNewEmailIsAlreadyInUse_thenShouldThrowException(){
        //Given
        PlayerDto playerDto = new PlayerDto()
                .setId(playerTwo.getId())
                .setFirstName(playerTwo.getFirstName())
                .setLastName(playerTwo.getLastName())
                .setEmail(playerThree.getEmail());
        String expectedMessagePart1 = "A player with the email address";
        String expectedMessagePart2 =  "already exists.";

        //When
        Exception exception = assertThrows(WalletException.DuplicateEntityException.class, () -> playerService.updatePlayer(playerDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));

    }

    @Test
    public void deletePlayer_whenPlayerIsDeleted_thenDeletedPlayerShouldBeReturned(){
        //Given
        long playerId = playerFour.getId();

        //When
        PlayerDto deletedPlayer = playerService.deletePlayer(playerId);

        //Then
        assertThat(deletedPlayer.getId()).isEqualTo(playerFour.getId());
        assertThat(deletedPlayer.getLastName()).isEqualTo(playerFour.getLastName());
        assertThat(deletedPlayer.getFirstName()).isEqualTo(playerFour.getFirstName());
        assertThat(deletedPlayer.getEmail()).isEqualTo(playerFour.getEmail());
    }

    @Test
    public void deletePlayer_whenPlayerDoesntExist_thenShouldThrowException(){
        //Given
        long playerId = 8;
        String expectedMessagePart1 = "Player with id";
        String expectedMessagePart2 =  "was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> playerService.deletePlayer(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));
    }

    @Test
    public void deletePlayer_whenPlayerDoesntGetDeleted_thenShouldThrowException(){
        //Given
        long playerId = playerFive.getId();
        String expectedMessagePart1 = "There was an issue deleting player with id";

        //When
        Exception exception = assertThrows(WalletException.InternalException.class, () -> playerService.deletePlayer(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
    }

    @Test
    public void deletePlayer_whenAccountDoesntGetDeleted_thenShouldThrowException(){
        //Given
        long playerId = playerSix.getId();
        String expectedMessagePart1 = "There was an issue deleting account with id";

        //When
        Exception exception = assertThrows(WalletException.InternalException.class, () -> playerService.deletePlayer(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
    }


}
