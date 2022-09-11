package com.elevator.elevating.restControllers;

import com.elevator.elevating.entities.Building;
import com.elevator.elevating.services.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/building")
@Tag(name = "Building", description = "This manages the buildings")
public class BuildingRestController {

    private BuildingService buildingService;

    public BuildingRestController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Operation(summary = "Add a building", description = "Adds a building to the system", tags = {"Building"})
    @PostMapping("/addBuilding")
    ResponseEntity<Building> addBuilding(
            @Parameter(description = "building details", required = true)
            @RequestBody Building building
    ){
        return ResponseEntity.ok(buildingService.addBuilding(building));
    }

    @Operation(summary = "Get all buildings", description = "Gets all buildings", tags = {"Building"})
    @GetMapping("/getBuildings")
    ResponseEntity<List<Building>> getAllBuildings(
    ){
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }
    @Operation(summary = "Get a buildings", description = "Gets a building", tags = {"Building"})
    @GetMapping("/getBuilding")
    ResponseEntity<Building> getBuilding(
          @RequestParam Long buildingId
    ){
        return ResponseEntity.ok(buildingService.getByBuildingId(buildingId));
    }
}
