package com.globo.globosat.service.strategy;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import com.globo.globosat.repository.NetworkRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class ArrangeInFirstNetworkTest {

    private NetworkRepository repo;
    private NetworkArrangeStrategy arranger;

    @Before
    public void setUp() throws Exception {
        repo = mock(NetworkRepository.class);
        arranger = new ArrangeInFirstNetwork(repo);
    }

    @Test
    public void must_arrange() throws Exception {
        arranger.arrange(new Collision(new Node("A"), new Node("B")));

        verify(repo, only()).save(any(Network.class));
    }

}