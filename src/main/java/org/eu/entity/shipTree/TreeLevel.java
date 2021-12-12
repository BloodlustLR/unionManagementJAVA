package org.eu.entity.shipTree;

import lombok.Data;
import org.eu.entity.Ship;

import java.util.List;

@Data
public class TreeLevel {

    private String id;

    private String type;

    private String label;

    private List<Ship> children;

}
