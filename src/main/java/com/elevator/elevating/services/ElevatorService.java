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
    public Elevator saveElevator(Elevator elevator){

        Elevator ele = elevatorRepository.findById(elevator.getId()).get();
        ele.setElevatorName(elevator.getElevatorName());
        ele.setDirection(elevator.getDirection());
        ele.setStartingFloor(elevator.getStartingFloor());
        ele.setDestinationFloor(elevator.getDestinationFloor());
        ele.setState(elevator.getState());

            elevatorRepository.save(ele);
            return elevator;

    }
    public Elevator saveNewElevator(Elevator elevator){

        Elevator ele = new Elevator();
        ele.setElevatorName(elevator.getElevatorName());
        ele.setDirection(elevator.getDirection());
        ele.setStartingFloor(elevator.getStartingFloor());
        ele.setCurrentFloor(elevator.getCurrentFloor());
        ele.setDestinationFloor(elevator.getDestinationFloor());
        ele.setState(elevator.getState());
        ele.setBuilding(elevator.getBuilding());
            elevatorRepository.save(ele);
            return elevator;

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
       moveElevatorToDestFloor(elevator);
        return CompletableFuture.completedFuture(elevatorRepository.findById(elevatorId).get());
    }

    public Direction setElevatorDirection(Elevator elevator) {
        if (elevator.getStartingFloor() > elevator.getDestinationFloor()) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }
    @Async("asyncExecutor")
    public void moveElevatorToDestFloor(Elevator elevator) throws InterruptedException{

        //going downwards
        if( elevator.getStartingFloor() > elevator.getDestinationFloor()) {
            for (int i = elevator.getStartingFloor() ; i > elevator.getDestinationFloor(); i--) {
                logger.info("elevator going down from floor: "+elevator.getStartingFloor()+" going to: "+ elevator.getDestinationFloor());
                elevatorMotion(elevator, i);
            }
        } else{

            for( int j = elevator.getStartingFloor(); j < elevator.getDestinationFloor(); j++){
                logger.info("elevator going up from floor: "+elevator.getStartingFloor()+" going to: "+ elevator.getDestinationFloor());
                elevatorMotion(elevator,j);
            }
        }
    }

    private void elevatorMotion(Elevator elevator, int i) throws InterruptedException {
        logger.info("elevator moving");
        saveState(elevator,State.MOVING, i);
        logger.info("elevator door opening");
        saveState(elevator,State.DOOR_OPENING, i);
        logger.info("elevator door closing");
        saveState(elevator,State.DOOR_CLOSING, i);
    }

    private void saveState(Elevator elevator,State state, int i) throws InterruptedException {
        logger.info("saving state");
        Elevator elefeta = new Elevator(elevator, i);
        elevator.setCurrentFloor(i);
        elevator.setState(state);

        logger.info("sleeping for state: "+ state.name());
        Thread.sleep(configureState(state));
        logger.info("awake for state: "+ state.name());
        saveNewElevator(elefeta);
        logger.info("finished saving state");
    }
}
