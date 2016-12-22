package com.globo.globosat.model;

import com.globo.globosat.controller.dto.CollisionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by raphael on 17/12/16.
 */
@Data
@AllArgsConstructor
public class Collision {
    private Node left;
    private Node right;

    public Collision(CollisionDTO collision) {
        left = new Node(collision.getLeftNodeId());
        right = new Node(collision.getRightNodeId());
    }
}
