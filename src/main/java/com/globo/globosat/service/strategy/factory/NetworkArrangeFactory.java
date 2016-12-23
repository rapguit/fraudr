package com.globo.globosat.service.strategy.factory;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.repository.NetworkRepository;
import com.globo.globosat.service.strategy.ArrangeInNetwork;
import com.globo.globosat.service.strategy.ArrangeInSameNetwork;
import com.globo.globosat.service.strategy.ArrangeInFirstNetwork;
import com.globo.globosat.service.strategy.NetworkArrangeStrategy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class NetworkArrangeFactory {

    @Autowired
    private NetworkRepository netRepo;

    public NetworkArrangeStrategy getArrangeStrategy(Collision collision){
        List<Network> networks = netRepo.findAll();

        if (networks.isEmpty()){
            return new ArrangeInFirstNetwork(netRepo);
        }

        if(netRepo.isCollisionBelongToSameNetwork(collision)){
            return new ArrangeInSameNetwork(netRepo);
        }

        return new ArrangeInNetwork(netRepo);
    }
}
