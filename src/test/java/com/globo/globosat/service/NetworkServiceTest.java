package com.globo.globosat.service;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import com.globo.globosat.repository.FileRepository;
import com.globo.globosat.repository.NetworkRepository;
import com.globo.globosat.service.strategy.factory.NetworkArrangeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Created by raphael on 21/12/16.
 */
public class NetworkServiceTest {

    private FileRepository fileRepo;
    private NetworkRepository netRepo;
    private NetworkArrangeFactory factory;
    private NetworkService service;

    @Before
    public void setUp() throws Exception {
        netRepo = new NetworkRepository();
        netRepo.init();
        factory = new NetworkArrangeFactory(netRepo);
        fileRepo = mock(FileRepository.class);

        List<Collision> collisions = new ArrayList<>();
        collisions.add(new Collision(new Node("A"), new Node("B")));
        collisions.add(new Collision(new Node("X"), new Node("Y")));

        when(fileRepo.loadCollisionsFromFile()).thenReturn(collisions);

        service = new NetworkService(fileRepo, netRepo, factory);
        service.init();
    }

    @Test
    public void arrange_in_network_must_union_networks() throws Exception {
        service.arrangeInNetwork(new Collision(new Node("A"), new Node("Y")));
        List<Network> all = netRepo.findAll();

        assertThat(all, hasSize(1));
        assertThat(all.get(0).getCollisions(), hasSize(3));
        assertThat(netRepo.findAllNodesOf(all.get(0)),
                hasItems(
                        new Node("A"),
                        new Node("B"),
                        new Node("X"),
                        new Node("Y")
                ));
    }

    @Test
    public void arrange_in_network_must_insert_in_same_network() throws Exception {
        Collision collision = new Collision(new Node("B"), new Node("A"));
        service.arrangeInNetwork(collision);
        List<Network> all = netRepo.findAll();
        Network net = netRepo.getNetworkOf(collision).get();

        assertThat(all, hasSize(2));
        assertThat(net.getCollisions(), hasSize(2));
        assertThat(netRepo.findAllNodesOf(net),
                hasItems(
                        new Node("A"),
                        new Node("B")
                )
        );
    }

    @Test
    public void arrange_in_network_must_insert_in_net_a() throws Exception {
        Collision collision = new Collision(new Node("B"), new Node("C"));
        service.arrangeInNetwork(collision);
        List<Network> all = netRepo.findAll();
        Network net = netRepo.getNetworkOf(collision).get();

        assertThat(all, hasSize(2));
        assertThat(net.getCollisions(), hasSize(2));
        assertThat(netRepo.findAllNodesOf(net),
                hasItems(
                        new Node("A"),
                        new Node("B"),
                        new Node("C")
                )
        );
    }

    @Test
    public void arrange_in_network_must_insert_in_net_b() throws Exception {
        Collision collision = new Collision(new Node("C"), new Node("X"));
        service.arrangeInNetwork(collision);
        List<Network> all = netRepo.findAll();
        Network net = netRepo.getNetworkOf(collision).get();

        assertThat(all, hasSize(2));
        assertThat(net.getCollisions(), hasSize(2));
        assertThat(netRepo.findAllNodesOf(net),
                hasItems(
                        new Node("X"),
                        new Node("Y"),
                        new Node("C")
                )
        );
    }

    @Test
    public void collision_belong_to_same_network() throws Exception {
        boolean collisionBelongToSameNetworkA = service.isCollisionBelongToSameNetwork(
                    new Collision(new Node("A"), new Node("B")
                ));

        boolean collisionBelongToSameNetworkB = service.isCollisionBelongToSameNetwork(
                    new Collision(new Node("Y"), new Node("X")
                ));

        assertThat(collisionBelongToSameNetworkA, is(true));
        assertThat(collisionBelongToSameNetworkB, is(true));
    }

    @Test
    public void collision_not_belong_to_same_network() throws Exception {
        boolean collisionBelongToSameNetwork = service.isCollisionBelongToSameNetwork(
                    new Collision(new Node("A"), new Node("Y")
                ));

        assertThat(collisionBelongToSameNetwork, is(false));
    }

    @Test
    public void must_find_all_networks() throws Exception {
        NetworkRepository netRepo = mock(NetworkRepository.class);
        NetworkArrangeFactory factory = mock(NetworkArrangeFactory.class);
        service = new NetworkService(fileRepo, netRepo, factory);

        service.getAllNetworks();

        verify(netRepo, only()).findAll();
    }

    @Test
    public void must_find_a_network() throws Exception {
        NetworkRepository netRepo = mock(NetworkRepository.class);
        NetworkArrangeFactory factory = mock(NetworkArrangeFactory.class);
        service = new NetworkService(fileRepo, netRepo, factory);

        service.getNetwork("123");

        verify(netRepo, only()).findById(eq("123"));
    }

}