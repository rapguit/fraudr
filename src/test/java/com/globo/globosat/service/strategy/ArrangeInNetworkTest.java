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

public class ArrangeInNetworkTest {

    private NetworkRepository repo;
    private NetworkArrangeStrategy arranger;

    @Before
    public void setUp() throws Exception {
        repo = mock(NetworkRepository.class);
        arranger = new ArrangeInNetwork(repo);
    }


    @Test
    public void must_arrange() throws Exception {
        Node b = new Node("B");
        Node c = new Node("C");

        when(repo.getNetworkOf(eq(b))).thenReturn(Optional.of(new Network(createNewListOfCollisions())));
        when(repo.getNetworkOf(eq(c))).thenReturn(Optional.empty());
        when(repo.save(any(Network.class))).thenReturn(new Network());

        arranger.arrange(new Collision(b, c));

        verify(repo, atLeastOnce()).save(any(Network.class));
    }

    private List<Collision> createNewListOfCollisions() {
        List<Collision> list = new ArrayList<>();
        list.add(new Collision(new Node("A"), new Node("B")));

        return list;
    }

}