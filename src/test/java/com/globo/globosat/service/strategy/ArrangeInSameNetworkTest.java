package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import com.globo.globosat.repository.NetworkRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArrangeInSameNetworkTest {

    private NetworkRepository repo;
    private NetworkArrangeStrategy arranger;

    @Before
    public void setUp() throws Exception {
        repo = mock(NetworkRepository.class);
        arranger = new ArrangeInSameNetwork(repo);
    }

    @Test
    public void must_arrange() throws Exception {
        List<Collision> collisions = new ArrayList<>();
        collisions.add(new Collision(new Node("B"), new Node("A")));
        Network net = new Network(collisions);

        when(repo.getNetworkOf(any(Collision.class))).thenReturn(Optional.of(net));
        when(repo.save(eq(net))).thenReturn(net);

        arranger.arrange(new Collision(new Node("A"), new Node("B")));

        verify(repo, atLeastOnce()).save(any(Network.class));
    }

    @Test(expected = IllegalStateException.class)
    public void not_arrange() throws Exception {
        when(repo.getNetworkOf(any(Collision.class))).thenReturn(Optional.empty());
        arranger.arrange(new Collision(new Node("A"), new Node("B")));

        verify(repo, atLeastOnce()).save(any(Network.class));
    }

}