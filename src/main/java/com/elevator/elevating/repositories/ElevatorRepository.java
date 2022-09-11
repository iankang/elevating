package com.elevator.elevating.repositories;

import com.elevator.elevating.entities.Building;
import com.elevator.elevating.entities.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator,Long> {

    Boolean existsByElevatorName(String elevatorName);

    List<Elevator> findByBuildingId(Long buildingId);
}
