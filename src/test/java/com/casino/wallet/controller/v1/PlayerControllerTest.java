package com.casino.wallet.controller.v1;

import com.casino.wallet.controller.v1.api.PlayerController;
import com.casino.wallet.controller.v1.request.RegisterPlayerRequest;
import com.casino.wallet.controller.v1.request.UpdatePlayerRequest;
import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlayerService playerService;

    private final HttpHeaders authHeader = createHeaders();

    HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = "walletuser" + ":" + "password123";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    //RegisterPlayer
    @Test
    public void RegisterPlayer_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email", is(playerDto.getEmail())))
                .andExpect(jsonPath("$.payload.firstName", is(playerDto.getFirstName())))
                .andExpect(jsonPath("$.payload.lastName", is(playerDto.getLastName())))
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.created", is(playerDto.getCreated().toString())));
    }

    @Test
    public void RegisterPlayer_whenCalledWithEmptyFirstName_thenShouldReturnBadRequest() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[First name cannot be empty.]")));
    }

    @Test
    public void RegisterPlayer_whenCalledWithEmptyLastName_thenShouldReturnBadRequest() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Last name cannot be empty.]")));
    }

    @Test
    public void RegisterPlayer_whenCalledWithEmptyEmail_thenShouldReturnBadRequest() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("")
                .setFirstName("Tester")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Email name cannot be empty.]")));
    }

    @Test
    public void RegisterPlayer_whenCalledWithInvalidEmail_thenShouldReturnBadRequest() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("InvalidEmail")
                .setFirstName("Tester")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Email must be valid.]")));
    }

    @Test
    public void RegisterPlayer_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        RegisterPlayerRequest registerPlayerRequest = new RegisterPlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());

        Mockito.when(playerService.registerPlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(post("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(registerPlayerRequest)))
                .andExpect(status().isUnauthorized());
    }

    //GetPlayers
    @Test
    public void GetPlayers_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        PlayerDto playerDtoOne = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        PlayerDto playerDtoTwo = new PlayerDto()
                .setEmail("Test2@gmail.com")
                .setFirstName("Tester2")
                .setLastName("Testsson2")
                .setId(2L)
                .setCreated(Instant.now());
        List<PlayerDto> playerDtoList = Arrays.asList(playerDtoOne, playerDtoTwo);

        Mockito.when(playerService.getAllPlayers())
                .thenReturn(playerDtoList);
        //When && Then
        mvc.perform(get("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.[0].email", is(playerDtoOne.getEmail())))
                .andExpect(jsonPath("$.payload.[0].firstName", is(playerDtoOne.getFirstName())))
                .andExpect(jsonPath("$.payload.[0].lastName", is(playerDtoOne.getLastName())))
                .andExpect(jsonPath("$.payload.[0].id", is(1)))
                .andExpect(jsonPath("$.payload.[0].created", is(playerDtoOne.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[1].email", is(playerDtoTwo.getEmail())))
                .andExpect(jsonPath("$.payload.[1].firstName", is(playerDtoTwo.getFirstName())))
                .andExpect(jsonPath("$.payload.[1].lastName", is(playerDtoTwo.getLastName())))
                .andExpect(jsonPath("$.payload.[1].id", is(2)))
                .andExpect(jsonPath("$.payload.[1].created", is(playerDtoTwo.getCreated().toString())));
    }

    @Test
    public void GetPlayers_whenCalledAndNoPlayersExist_thenShouldReturnNotFound() throws Exception {
        //Given
        List<PlayerDto> playerDtoList = Collections.emptyList();

        Mockito.when(playerService.getAllPlayers())
                .thenReturn(playerDtoList);
        //When && Then
        mvc.perform(get("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.errors", is("No players found.")));

    }

    @Test
    public void GetPlayers_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        PlayerDto playerDtoOne = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        PlayerDto playerDtoTwo = new PlayerDto()
                .setEmail("Test2@gmail.com")
                .setFirstName("Tester2")
                .setLastName("Testsson2")
                .setId(2L)
                .setCreated(Instant.now());
        List<PlayerDto> playerDtoList = Arrays.asList(playerDtoOne, playerDtoTwo);

        Mockito.when(playerService.getAllPlayers())
                .thenReturn(playerDtoList);
        //When && Then
        mvc.perform(get("/v1/player/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //GetPlayer
    @Test
    public void GetPlayer_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.getPlayerById(playerId))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(get("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email", is(playerDto.getEmail())))
                .andExpect(jsonPath("$.payload.firstName", is(playerDto.getFirstName())))
                .andExpect(jsonPath("$.payload.lastName", is(playerDto.getLastName())))
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.created", is(playerDto.getCreated().toString())));
    }

    @Test
    public void GetPlayer_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.getPlayerById(playerId))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(get("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //UpdatePlayer
    @Test
    public void UpdatePlayer_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email", is(playerDto.getEmail())))
                .andExpect(jsonPath("$.payload.firstName", is(playerDto.getFirstName())))
                .andExpect(jsonPath("$.payload.lastName", is(playerDto.getLastName())))
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.created", is(playerDto.getCreated().toString())));
    }

    @Test
    public void UpdatePlayer_whenCalledWithEmptyFirstName_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("")
                .setLastName("Testsson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[First name cannot be empty.]")));
    }

    @Test
    public void UpdatePlayer_whenCalledWithEmptyLastName_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Last name cannot be empty.]")));
    }

    @Test
    public void UpdatePlayer_whenCalledWithEmptyEmail_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("")
                .setFirstName("Tester")
                .setLastName("Testersson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Email cannot be empty.]")));
    }

    @Test
    public void UpdatePlayer_whenCalledWithInvalidEmail_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("InvalidEmail")
                .setFirstName("Tester")
                .setLastName("Testersson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Email must be valid.]")));
    }

    @Test
    public void UpdatePlayer_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        UpdatePlayerRequest updatePlayerRequest = new UpdatePlayerRequest()
                .setEmail("InvalidEmail")
                .setFirstName("Tester")
                .setLastName("Testersson");
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.updatePlayer(any(PlayerDto.class)))
                .thenReturn(playerDto);

        //When && Then
        mvc.perform(put("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(updatePlayerRequest)))
                .andExpect(status().isUnauthorized());
    }

    //DeletePlayer
    @Test
    public void DeletePlayer_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.deletePlayer(playerId))
                .thenReturn(playerDto);

        //When && Then

        mvc.perform(delete("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email", is(playerDto.getEmail())))
                .andExpect(jsonPath("$.payload.firstName", is(playerDto.getFirstName())))
                .andExpect(jsonPath("$.payload.lastName", is(playerDto.getLastName())))
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.created", is(playerDto.getCreated().toString())));
    }

    @Test
    public void DeletePlayer_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        PlayerDto playerDto = new PlayerDto()
                .setEmail("Test@gmail.com")
                .setFirstName("Tester")
                .setLastName("Testsson")
                .setId(1L)
                .setCreated(Instant.now());
        Mockito.when(playerService.deletePlayer(playerId))
                .thenReturn(playerDto);

        //When && Then

        mvc.perform(delete("/v1/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
