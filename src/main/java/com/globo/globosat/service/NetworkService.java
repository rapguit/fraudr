package com.globo.globosat.service;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.FileRepository;
import com.globo.globosat.repository.NetworkRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.logicalAnd;
import static java.util.Arrays.asList;

/**
 * Created by raphael on 17/12/16.
 */
@Service
@AllArgsConstructor
public class NetworkService {

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private NetworkRepository netRepo;

    @PostConstruct
    public void init(){
        List<Collision> collisions = fileRepo.loadCollisionsFromFile();

        for(Collision collision: collisions) {
            arrangeInNetwork(collision);
        }
    }

    /**
     * Add new collision between two nodes.
     * Add new collision in network and rearrange networks
     * @param collision
     */
    public Network arrangeInNetwork(Collision collision) {
        List<Network> networks = netRepo.findAll();

        if (networks.isEmpty()){
            return netRepo.save(new Network(new ArrayList<>(asList(collision))));
        }
        else if(isCollisionBelongToSameNetwork(collision)){
            return netRepo.getNetworkOf(collision).map(network -> {
                network.addCollision(collision);
                return netRepo.save(network);
            }).orElseThrow(IllegalStateException::new);
        }else{
            Optional<Network> netA = netRepo.getNetworkOf(collision.getLeft());
            Optional<Network> netB = netRepo.getNetworkOf(collision.getRight());

            if(logicalAnd(netA.isPresent(), netB.isPresent())){
                Network network = netA.get();
                network.addCollision(collision);
                netB.get().getCollisions().forEach(c -> network.addCollision(c));
                netRepo.remove(netB.get().getId());
                return netRepo.save(network);
            }
            if(logicalAnd(!netA.isPresent(), netB.isPresent())){
                Network network = netB.get();
                network.addCollision(collision);
                return netRepo.save(network);
            }
            if(logicalAnd(netA.isPresent(), !netB.isPresent())){
                Network network = netA.get();
                network.addCollision(collision);
                return netRepo.save(network);
            }
            if(logicalAnd(!netA.isPresent(), !netB.isPresent())){
                return netRepo.save(new Network(new ArrayList<>(asList(collision))));
            }
        }

        throw new IllegalStateException();
    }

    /**
     * Answer if two nodes belong to the same collision network.
     * @param collision
     * @return
     */
    public boolean isCollisionBelongToSameNetwork(Collision collision) {
        return netRepo.getNetworkOf(collision).isPresent();
    }

    public Network getNetwork(String id) {
        return netRepo.findById(id);
    }

    public List<Network> getAllNetworks() {
        return netRepo.findAll();
    }
}
