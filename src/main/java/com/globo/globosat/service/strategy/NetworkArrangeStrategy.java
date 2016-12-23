package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;

public interface NetworkArrangeStrategy {
    Network arrange(Collision collision);
}
