package com.globo.globosat.repository;

import com.globo.globosat.exception.NetworkNotFoundException;
import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.logicalAnd;

/**
 * Created by raphael on 17/12/16.
 */
@Repository
public class NetworkRepository {

    private static Map<String, Network> networks;

    @PostConstruct
    public void init() {
        networks = new HashMap<>();
    }

    public void save(List<Network> networks) {
        networks.forEach(this::save);
    }

    public Network save(Network network) {
        if (network.getId() == null) {
            String uuid = UUID.randomUUID().toString();
            network.setId(uuid);
            networks.put(uuid, network);
        }else{
            networks.put(network.getId(), network);
        }

        return network;
    }

    public List<Network> findAll() {
        return new ArrayList<>(networks.values());
    }

    public List<Node> findAllNodesOf(Network network) {
        return network.getCollisions().stream()
                .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void remove(String id) {
        networks.remove(id);
    }

    public Optional<Network> getNetworkOf(Collision collision) {
        List<Network> networks = findAll();
        for(Network network: networks) {
            List<Node> nodes = findAllNodesOf(network);

            boolean leftMatched = !nodes.stream()
                    .filter(n -> n.getId().equals(collision.getLeft().getId()))
                    .collect(Collectors.toList()).isEmpty();

            boolean rightMatched = !nodes.stream()
                    .filter(n -> n.getId().equals(collision.getRight().getId()))
                    .collect(Collectors.toList()).isEmpty();

            if(logicalAnd(leftMatched, rightMatched)) return Optional.of(network);
        }

        return Optional.empty();
    }

    public Optional<Network> getNetworkOf(Node node) {
        List<Network> networks = findAll().stream().collect(Collectors.toList());

        for(Network network: networks) {
            List<Node> nodes = findAllNodesOf(network);
            if (nodes.contains(node)){
                return Optional.of(network);
            }
        }
        return Optional.empty();
    }

    public Network findById(String id) {
        return Optional.ofNullable(networks.get(id)).orElseThrow(NetworkNotFoundException::new);
    }

    public boolean isCollisionBelongToSameNetwork(Collision collision) {
        return getNetworkOf(collision).isPresent();
    }
}
