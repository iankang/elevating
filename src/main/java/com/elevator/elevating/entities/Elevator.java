package com.elevator.elevating.entities;

import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "elevator")
public class Elevator extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Building building;
    @Column(unique = true)
    String elevatorName;
     Direction direction;
     State state;

     int startingFloor;
     int destinationFloor;

    public Elevator(String elevatorName, Direction direction, State state, int startingFloor, int destinationFloor) {
        this.elevatorName = elevatorName;
        this.direction = direction;
        this.state = state;
        this.startingFloor = startingFloor;
        this.destinationFloor = destinationFloor;
    }
}
