package com.elevator.elevating.services;

import com.elevator.elevating.entities.Building;
import com.elevator.elevating.entities.Elevator;
import com.elevator.elevating.models.Direction;
import com.elevator.elevating.models.State;
import com.elevator.elevating.repositories.ElevatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ElevatorService {

    private final Logger logger = LoggerFactory.getLogger(ElevatorService.class);
    private ElevatorRepository elevatorRepository;
    private BuildingService buildingService;

    public ElevatorService(ElevatorRepository elevatorRepository, BuildingService buildingService) {
        this.elevatorRepository = elevatorRepository;
        this.buildingService = buildingService;
    }

    public Direction configureDirection(boolean isUp){
        if (isUp) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    public Long configureState(State state) {
        switch (state) {
            case MOVING:
                return 5000L;
            case STOPPED:
                return 1000L;
            case DOOR_OPENING:
            case DOOR_CLOSING:
                return 2000L;
        }
        return null;
    }

    public Elevator saveElevator(String elevatorName, Elevator elevator){

        if(!doesElevatorExist(elevatorName)) {
            elevatorRepository.save(elevator);
            return elevator;
        }
        return  null;
    }
    public Elevator saveElevator(Long elevatorId, Elevator elevator){

        Elevator ele = elevatorRepository.findById(elevatorId).get();
        ele.setElevatorName(elevator.getElevatorName());
        ele.setDirection(elevator.getDirection());
        ele.setStartingFloor(elevator.getStartingFloor());
        ele.setDestinationFloor(elevator.getDestinationFloor());
        ele.setState(elevator.getState());

            elevatorRepository.save(ele);return elevator;

    }

    public Elevator saveBuildingElevator(Long buildingId, Elevator elevator){
        Building building = buildingService.getByBuildingId(buildingId);
        if (building != null){

            elevator.setBuilding(building);
            return elevatorRepository.save(elevator);
        }
        return null;
    }

    public Boolean doesElevatorExist(String elevatorName){
        if (elevatorRepository.existsByElevatorName(elevatorName)){
            return true;
        }
        return false;
    }

    public List<Elevator> getAllElevatorsByBuildingId(Long buildingId) {
        return  elevatorRepository.findByBuildingId(buildingId);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Elevator> startElevator(Long elevatorId) throws InterruptedException{
        logger.info("started");
        Elevator elevator = elevatorRepository.findById(elevatorId).get();
        elevator.setState(State.MOVING);
        saveElevator(elevatorId,elevator);
        Thread.sleep(configureState(State.MOVING));
        return CompletableFuture.completedFuture(elevatorRepository.findById(elevatorId).get());
    }
}
