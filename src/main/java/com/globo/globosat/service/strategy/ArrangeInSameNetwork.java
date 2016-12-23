package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.NetworkRepository;

public class ArrangeInSameNetwork implements NetworkArrangeStrategy {
    private final NetworkRepository netRepo;

    public ArrangeInSameNetwork(NetworkRepository netRepo) {
        this.netRepo = netRepo;
    }

    @Override
    public Network arrange(Collision collision) {
        return netRepo.getNetworkOf(collision).map(network -> {
            network.addCollision(collision);
            return netRepo.save(network);
        }).orElseThrow(IllegalStateException::new);
    }
}
