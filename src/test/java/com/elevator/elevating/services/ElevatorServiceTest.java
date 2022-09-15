package com.elevator.elevating.services;

import com.elevator.elevating.entities.Building;
import com.elevator.elevating.entities.Elevator;
import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.elevator.elevating.repositories.ElevatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ElevatorServiceTest {

    @Mock
    ElevatorRepository elevatorRepository;

    @InjectMocks
    ElevatorService elevatorService;

    Building building;
    Elevator elevator;

    @BeforeEach
    public void setup(){
        building = new Building("bilding",12);
        elevator = new Elevator(building,"elevator", State.STOPPED,8,0);
    }
    @Test
    void givenBuildingAndElevator_whenGetDirectionStartFloorGreaterThanDestination_thenReturnDown() {

        assertEquals(elevator.getDirection(),Direction.DOWN);
    }
}