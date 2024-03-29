package br.pucminas.morada.controllers;

import br.pucminas.morada.models.property.*;
import br.pucminas.morada.models.property.dto.PropertyCreateDTO;
import br.pucminas.morada.models.property.dto.PropertyDTO;
import br.pucminas.morada.models.property.dto.PropertyUpdateDTO;
import br.pucminas.morada.models.user.UserRole;
import br.pucminas.morada.security.UserSpringSecurity;
import br.pucminas.morada.services.PropertyService;
import br.pucminas.morada.services.UserService;
import br.pucminas.morada.services.exceptions.AuthorizationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/properties")
@Validated
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> search(
            @RequestParam(name = "status", required = false) PropertyStatus status,
            @RequestParam(name = "type", required = false) PropertyType[] types,
            @RequestParam(name = "bedrooms", required = false) Integer minimumBedrooms,
            @RequestParam(name = "bathrooms", required = false) Integer minimumBathrooms,
            @RequestParam(name = "garageSpaces", required = false) Integer minimumGarageSpaces
    ) {

        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        List<Specification<Property>> specifications = new ArrayList<>();

        if ((userSpringSecurity == null || !userSpringSecurity.hasRole(UserRole.ADMIN)) && status != PropertyStatus.APPROVED) {
            throw new AuthorizationException();
        }

        if (status != null) {
            specifications.add((root, query, builder) -> builder.equal(root.get("status"), status));
        }

        if (types != null) {
            specifications.add((root, query, builder) -> root.get("type").in((Object[]) types));
        }

        if (minimumBedrooms != null) {
            specifications.add((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("bedrooms"), minimumBedrooms));
        }

        if (minimumBathrooms != null) {
            specifications.add((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("bathrooms"), minimumBedrooms));
        }

        if (minimumGarageSpaces != null) {
            specifications.add((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("garageSpaces"), minimumGarageSpaces));
        }

        Specification<Property> specification = Specification.allOf(specifications);
        List<Property> properties = this.propertyService.findAll(specification);

        List<PropertyDTO> propertyDTOs = properties.stream()
                .map(Property::toDTO)
                .toList();

        return ResponseEntity.ok(propertyDTOs);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> findById(@PathVariable Long id) {
        Property property = this.propertyService.findById(id);
        return ResponseEntity.ok(property.toDTO());
    }

    @GetMapping("/user")
    public ResponseEntity<List<PropertyDTO>> findAllByUser() {
        List<Property> properties = this.propertyService.findAllByUser();
        return ResponseEntity.ok(properties.stream().map(Property::toDTO).toList());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PropertyCreateDTO propertyCreateDTO) {

        Property property = propertyCreateDTO.toEntity(Property.class);
        Property newProperty = this.propertyService.create(property);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProperty.getId())
                .toUri();

        return ResponseEntity.created(uri).build();

    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(
            @Valid @RequestBody PropertyUpdateDTO propertyUpdateDTO,
            @PathVariable Long id
    ) {

        this.propertyService.update(id, propertyUpdateDTO.toEntity(Property.class));
        return ResponseEntity.noContent().build();

    }

}
