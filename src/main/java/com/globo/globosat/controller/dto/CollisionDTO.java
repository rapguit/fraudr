package com.globo.globosat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by raphael on 21/12/16.
 */
@Data
@AllArgsConstructor
public class CollisionDTO {
    private String leftNodeId;
    private String rightNodeId;
}
