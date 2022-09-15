package com.elevator.elevating.entities;

import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;


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


    String elevatorName;
     Direction direction;
     State state;

     int startingFloor;
     int destinationFloor;

     int currentFloor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Building building;

    public Elevator(String elevatorName, Direction direction, State state, int startingFloor, int destinationFloor) {


        this.elevatorName = elevatorName;
        this.direction = direction;
        this.state = state;
        this.startingFloor = startingFloor;
        this.currentFloor = startingFloor;
        this.state = State.STOPPED;
        this.destinationFloor = destinationFloor;
    }
    public Elevator(Building building,String elevatorName,  State state, int startingFloor, int destinationFloor) {

        this.building = building;
        this.elevatorName = elevatorName;
        this.direction = (startingFloor > destinationFloor)? Direction.DOWN : Direction.UP;
        this.state = state;
        this.startingFloor = startingFloor;
        this.currentFloor = startingFloor;
        this.state = State.STOPPED;
        this.destinationFloor = destinationFloor;
    }

    public Elevator(String elevatorName, Building bilding){
        SecureRandom random = new SecureRandom();
        int starterFloor = random.nextInt(bilding.getFloors());
        int finisherFloor = random.nextInt(bilding.getFloors());
        this.elevatorName = elevatorName;
        this.startingFloor = starterFloor;
        this.destinationFloor = finisherFloor;
        this.currentFloor = starterFloor;
        // if starting floor is greater than destination floor the direction is down and up otherwise.
        this.direction = (starterFloor > finisherFloor) ? Direction.DOWN : Direction.UP;
        this.state = State.STOPPED;
        this.building = bilding;
    }

    public Elevator(Elevator elevator,  int currentFloor){
        this.elevatorName = elevator.getElevatorName();
        this.direction = elevator.getDirection();
        this.state = elevator.getState();
        this.startingFloor = elevator.getStartingFloor();
        this.currentFloor= currentFloor;
        this.state = State.STOPPED;
        this.building = elevator.getBuilding();
    }
}
