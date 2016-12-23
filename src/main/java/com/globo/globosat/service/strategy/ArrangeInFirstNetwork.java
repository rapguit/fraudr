package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.NetworkRepository;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class ArrangeInFirstNetwork implements NetworkArrangeStrategy {

    private final NetworkRepository netRepo;

    public ArrangeInFirstNetwork(NetworkRepository netRepo) {
        this.netRepo = netRepo;
    }

    @Override
    public Network arrange(Collision collision) {
        return netRepo.save(new Network(new ArrayList<>(asList(collision))));
    }
}
