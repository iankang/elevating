package com.elevator.elevating.services;

import com.elevator.elevating.entities.Building;
import com.elevator.elevating.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public Building addBuilding(Building building) {
        buildingRepository.save(building);
        return building;
    }

    public Building updateBuilding(Long buildingId, Building building){

        if (existsById(buildingId)){
            Building buildingDB = buildingRepository.findById(buildingId).get();
            buildingRepository.save(buildingDB);
            return  building;
        }
        return  null;
    }

    public boolean existsById(Long buildingId) {
        return buildingRepository.existsById(buildingId);
    }

    public List<Building> getAllBuildings(){
        return buildingRepository.findAll();
    }

    public Building getByBuildingId(Long buildingId){

        return buildingRepository.findById(buildingId).get();
    }
}
