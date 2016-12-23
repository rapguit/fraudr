package com.globo.globosat.service;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.FileRepository;
import com.globo.globosat.repository.NetworkRepository;
import com.globo.globosat.service.strategy.factory.NetworkArrangeFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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

    @Autowired
    NetworkArrangeFactory factory;

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
        return factory.getArrangeStrategy(collision).arrange(collision);
    }

    /**
     * Answer if two nodes belong to the same collision network.
     * @param collision
     * @return
     */
    public boolean isCollisionBelongToSameNetwork(Collision collision) {
        return netRepo.isCollisionBelongToSameNetwork(collision);
    }

    public Network getNetwork(String id) {
        return netRepo.findById(id);
    }

    public List<Network> getAllNetworks() {
        return netRepo.findAll();
    }
}
