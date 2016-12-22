package com.globo.globosat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raphael on 17/12/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Network {
    private String id;
    private List<Collision> collisions = new ArrayList<>();

    public Network(List<Collision> collisions) {
        this.collisions = collisions;
    }

    public void addCollision(Collision collision){
        collisions.add(collision);
    }
}
