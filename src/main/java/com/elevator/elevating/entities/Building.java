package com.elevator.elevating.entities;

import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "building")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String buildingName;
    int floors;

    public Building(String buildingName, int floors) {
        this.buildingName = buildingName;
        this.floors = floors;
    }
}
