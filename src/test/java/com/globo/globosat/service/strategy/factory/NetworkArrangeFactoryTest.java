package com.globo.globosat.service.strategy.factory;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import com.globo.globosat.model.vo.CollisionCheck;
import com.globo.globosat.repository.NetworkRepository;
import com.globo.globosat.service.strategy.ArrangeInNetwork;
import com.globo.globosat.service.strategy.ArrangeInFirstNetwork;
import com.globo.globosat.service.strategy.ArrangeInSameNetwork;
import com.globo.globosat.service.strategy.NetworkArrangeStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkArrangeFactoryTest {

    @Mock private NetworkRepository netRepo;
    @InjectMocks private NetworkArrangeFactory factory;

    @Test
    public void should_get_new_network_arrange_strategy() throws Exception {
        when(netRepo.findAll()).thenReturn(Collections.emptyList());
        NetworkArrangeStrategy arrangeStrategy = factory.getArrangeStrategy(new Collision(new Node("A"), new Node("B")));

        assertThat(arrangeStrategy, instanceOf(ArrangeInFirstNetwork.class));
    }

    @Test
    public void should_get_same_network_arrange_strategy() throws Exception {
        when(netRepo.findAll()).thenReturn(asList(new Network()));
        when(netRepo.isCollisionBelongToSameNetwork(Matchers.any(Collision.class))).thenReturn(new CollisionCheck(true, "123"));

        NetworkArrangeStrategy arrangeStrategy = factory.getArrangeStrategy(new Collision(new Node("A"), new Node("B")));

        assertThat(arrangeStrategy, instanceOf(ArrangeInSameNetwork.class));
    }

    @Test
    public void should_get_default_network_arrange_strategy() throws Exception {
        when(netRepo.findAll()).thenReturn(asList(new Network()));
        when(netRepo.isCollisionBelongToSameNetwork(Matchers.any(Collision.class))).thenReturn(new CollisionCheck(false, ""));

        NetworkArrangeStrategy arrangeStrategy = factory.getArrangeStrategy(new Collision(new Node("A"), new Node("B")));

        assertThat(arrangeStrategy, instanceOf(ArrangeInNetwork.class));
    }

}