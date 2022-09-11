package com.elevator.elevating.restControllers;


import com.elevator.elevating.entities.Elevator;
import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.elevator.elevating.services.ElevatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/elevator")
@Tag(name = "elevator", description = "This manages the elevators")
public class ElevatorRestController {

    private final Logger logger = LoggerFactory.getLogger(ElevatorRestController.class);
    private ElevatorService elevatorService;

    public ElevatorRestController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @Operation(summary = "Get all elevators", description = "Gets all elevators in a building", tags = {"elevator"})
    @GetMapping("/getAllElevators")
    public List<Elevator> getAllElevators(Long buildingId) {
        return elevatorService.getAllElevatorsByBuildingId(buildingId);
    }

    @Operation(summary = "Add an elevator", description = "Adds an elevator to the building", tags = {"elevator"})
    @PostMapping("/addElevator")
    public Elevator addElevator(
            @RequestParam Long buildingId,
            @RequestParam String elevatorName,
            @RequestParam Direction direction,
            @RequestParam State state,
            @RequestParam int startingFloor,
            @RequestParam int destinationFloor
    ){
        Elevator ele = new Elevator(elevatorName,direction,state,startingFloor,destinationFloor);
        return elevatorService.saveBuildingElevator(buildingId,ele);
    }


    @Operation(summary = "Start an elevator", description = "Starts an elevator to the building", tags = {"elevator"})
    @PostMapping("/startElevator")
    public ResponseEntity<Elevator> startElevator(
            @RequestParam  Long elevatorId
    ) throws InterruptedException, ExecutionException{
        logger.info("starting elevator");

        CompletableFuture<Elevator> elevatorCompletableFuture = elevatorService.startElevator(elevatorId);

        CompletableFuture.allOf(elevatorCompletableFuture);
        logger.info("returned: "+ elevatorCompletableFuture.get());
        return ResponseEntity.ok(elevatorCompletableFuture.get());
    }
}
