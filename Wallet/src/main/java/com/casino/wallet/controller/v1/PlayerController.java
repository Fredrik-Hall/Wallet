package com.casino.wallet.controller.v1;

import com.casino.wallet.model.player.Player;
import com.casino.wallet.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/Player")
public class PlayerController {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/")
    public void CreatePlayer(@RequestBody Player player){
        System.out.println(player.toString());
        playerRepository.save(player);
    }

    @GetMapping("/")
    public void GetPlayers(){
    }

    @GetMapping("/{PlayerId}")
    public void GetPlayer(@PathVariable long PlayerId){
        System.out.println((playerRepository.findByplayerId(PlayerId)).toString());
    }

    @PutMapping("/{PlayerId}")
    public void UpdatePlayer(@PathVariable int PlayerId){}

    @DeleteMapping("/{PlayerId}")
    public void DeletePlayer(@PathVariable int PlayerId){}
}
