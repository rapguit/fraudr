package com.globo.globosat.controller;

import com.globo.globosat.controller.dto.CollisionDTO;
import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by raphael on 21/12/16.
 */
@RestController
@RequestMapping("/fraudr/network")
public class FraudrController {

    @Autowired
    private NetworkService service;

    @RequestMapping("{id}")
    public Network get(@Valid @PathVariable final String id) {
        return service.getNetwork(id);
    }

    @RequestMapping
    public List<Network> getAll() {
        return service.getAllNetworks();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addCollision(@Valid @RequestBody final CollisionDTO collision, UriComponentsBuilder uriBuilder) {

        String id = service.arrangeInNetwork(new Collision(collision)).getId();

        return ResponseEntity
                .created(uriBuilder.path("/fraudr/network/{id}")
                        .buildAndExpand(id)
                        .toUri())
                .build();
    }

    @RequestMapping(path = "collision", method = RequestMethod.POST)
    public Boolean checkCollision(@Valid @RequestBody final CollisionDTO collision) {
        return service.isCollisionBelongToSameNetwork(new Collision(collision));
    }

}
