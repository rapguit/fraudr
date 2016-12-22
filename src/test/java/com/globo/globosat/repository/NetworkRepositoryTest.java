package com.globo.globosat.repository;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.model.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by raphael on 20/12/16.
 */
public class NetworkRepositoryTest {

    private NetworkRepository repo;

    @Before
    public void setUp() throws Exception {
        repo = new NetworkRepository();
        repo.init();
    }

    @Test
    public void save_networks() throws Exception {
        repo.save(asList(new Network(), new Network()));
        List<Network> all = repo.findAll();

        assertThat(all, hasSize(2));
        assertThat(all.get(0), notNullValue());
        assertThat(all.get(1), notNullValue());
    }

    @Test
    public void save_network() throws Exception {
        repo.save(new Network());
        List<Network> all = repo.findAll();

        assertThat(all, hasSize(1));
        assertThat(all.get(0).getId(), notNullValue());
    }

    @Test
    public void find_all_nodes_of_network() throws Exception {
        Network saved = repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));

        List<Node> allNodes = repo.findAllNodesOf(saved);
        assertThat(allNodes, hasSize(3));
    }

    @Test
    public void remove_network() throws Exception {
        Network saved = repo.save(new Network());
        repo.remove(saved.getId());

        List<Network> nets = repo.findAll();
        assertThat(nets, hasSize(0));
    }

    @Test
    public void get_network_of_collision() throws Exception {
        Network savedA = repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> net = repo.getNetworkOf(new Collision(new Node("B"), new Node("C")));

        assertThat(net.isPresent(), is(true));
        assertThat(net.get().getId(), equalTo(savedA.getId()));
    }

    @Test
    public void not_find_network_of_collision_with_strange_nodes() throws Exception {
        repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> net = repo.getNetworkOf(new Collision(new Node("G"), new Node("H")));

        assertThat(net.isPresent(), is(false));
    }

    @Test
    public void not_find_network_of_collision_with_left_strange_node() throws Exception {
        repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> net = repo.getNetworkOf(new Collision(new Node("G"), new Node("X")));

        assertThat(net.isPresent(), is(false));
    }

    @Test
    public void not_find_network_of_collision_with_right_strange_node() throws Exception {
        Network savedA = repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> net = repo.getNetworkOf(new Collision(new Node("A"), new Node("J")));

        assertThat(net.isPresent(), is(false));
    }

    @Test
    public void get_network_of_node() throws Exception {
        Network savedA = repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> found = repo.getNetworkOf(new Node("C"));

        assertThat(found.isPresent(), is(true));
        assertThat(found.get().getId(), equalTo(savedA.getId()));
    }

    @Test
    public void get_network_of_node_with_exclusion() throws Exception {
        Network savedA = repo.save(new Network(asList(
                new Collision(new Node("A"), new Node("B")),
                new Collision(new Node("B"), new Node("C"))
        )));
        repo.save(new Network(asList(
                new Collision(new Node("Z"), new Node("Y")),
                new Collision(new Node("Y"), new Node("X"))
        )));

        Optional<Network> found = repo.getNetworkOf(new Node("C"));

        assertThat(found.isPresent(), is(true));
        assertThat(found.get().getId(), equalTo(savedA.getId()));
    }

}