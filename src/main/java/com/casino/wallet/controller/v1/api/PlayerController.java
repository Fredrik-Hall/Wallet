package com.casino.wallet.controller.v1.api;

import com.casino.wallet.controller.v1.request.RegisterPlayerRequest;
import com.casino.wallet.controller.v1.request.UpdatePlayerRequest;
import com.casino.wallet.dto.model.PlayerDto;
import com.casino.wallet.dto.response.Response;
import com.casino.wallet.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/v1/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/")
    public Response RegisterPlayer(@RequestBody @Valid RegisterPlayerRequest registerPlayerRequest){
        PlayerDto playerDto = new PlayerDto()
                .setEmail(registerPlayerRequest.getEmail())
                .setFirstName(registerPlayerRequest.getFirstName())
                .setLastName(registerPlayerRequest.getLastName());

        return Response.ok().setPayload(playerService.registerPlayer(playerDto));
    }

    @GetMapping("/")
    public Response GetPlayers(){
        List <PlayerDto> playerDtos = playerService.getAllPlayers();
        if (!playerDtos.isEmpty()){
            return Response.ok().setPayload(playerDtos);
        }
        return Response.notFound().setErrors("No players found.");
    }

    @GetMapping("/{playerId}")
    public Response GetPlayer(@PathVariable Long playerId){
        return Response.ok().setPayload(playerService.getPlayerById(playerId));

    }

    @PutMapping("/{playerId}")
    public Response UpdatePlayer(@RequestBody @Valid UpdatePlayerRequest updatePlayerRequest, @PathVariable Long playerId){
        PlayerDto playerDto = new PlayerDto()
                .setEmail(updatePlayerRequest.getEmail())
                .setFirstName(updatePlayerRequest.getFirstName())
                .setLastName(updatePlayerRequest.getLastName())
                .setId(playerId);

        return Response.ok().setPayload(playerService.updatePlayer(playerDto));
    }

    @DeleteMapping("/{playerId}")
    public Response DeletePlayer(@PathVariable Long playerId){
        return Response.ok().setPayload(playerService.deletePlayer(playerId));
    }
}
