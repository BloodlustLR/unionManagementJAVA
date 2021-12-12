package org.eu.entity.shipTree;

import lombok.Data;

import java.util.List;

@Data
public class TreeType {

    private String id;

    private String label;

    private List<TreeLevel> children;


}
