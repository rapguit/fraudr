package com.globo.globosat.controller;

import com.globo.globosat.controller.dto.CollisionDTO;
import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Network;
import com.globo.globosat.service.NetworkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by raphael on 22/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class FraudrControllerTest {

    @Mock private NetworkService service;
    @InjectMocks private FraudrController controller;

    @Test
    public void must_get_by_id() throws Exception {
        controller.get("123");
        verify(service, only()).getNetwork(eq("123"));
    }

    @Test
    public void must_get_all_networks() throws Exception {
        controller.getAll();
        verify(service, only()).getAllNetworks();
    }

    @Test
    public void must_add_a_collision_in_network() throws Exception {
        CollisionDTO dto = new CollisionDTO("A", "B");
        when(service.arrangeInNetwork(eq(new Collision(dto)))).thenReturn(new Network("123", Collections.emptyList()));

        ResponseEntity responseEntity = controller.addCollision(dto, UriComponentsBuilder.fromUriString("host.test"));

        verify(service, only()).arrangeInNetwork(eq(new Collision(dto)));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("host.test/fraudr/network/123"));

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(responseEntity.getHeaders(), equalTo(httpHeaders));
    }

    @Test
    public void must_check_collision_belongs_to_a_network() throws Exception {
        when(service.isCollisionBelongToSameNetwork(any(Collision.class))).thenReturn(true);
        Boolean resp = controller.checkCollision(mock(CollisionDTO.class));

        verify(service, only()).isCollisionBelongToSameNetwork(any(Collision.class));

        assertThat(resp, is(true));
    }

}