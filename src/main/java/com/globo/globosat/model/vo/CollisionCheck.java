package com.globo.globosat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by raphael on 31/12/16.
 */
@Data
@AllArgsConstructor
public class CollisionCheck {
    private boolean belongsToNetwork;
    private String networkId;
}
