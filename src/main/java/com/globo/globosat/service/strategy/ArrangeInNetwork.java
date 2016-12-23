package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.NetworkRepository;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.Boolean.logicalAnd;
import static java.util.Arrays.asList;

public class ArrangeInNetwork implements NetworkArrangeStrategy {
    private final NetworkRepository netRepo;

    public ArrangeInNetwork(NetworkRepository netRepo) {
        this.netRepo = netRepo;
    }

    @Override
    public Network arrange(Collision collision) {
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

        return netRepo.save(new Network(new ArrayList<>(asList(collision))));
    }
}
