package br.pucminas.morada.controllers;

import br.pucminas.morada.models.rental.Rental;
import br.pucminas.morada.models.rental.dto.RentalCreateDTO;
import br.pucminas.morada.models.rental.dto.RentalDTO;
import br.pucminas.morada.models.rental.dto.RentalUpdateDTO;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.pucminas.morada.services.RentalService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
@Validated
public class RentalController {


    @Autowired
    private RentalService rentalService;


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody RentalCreateDTO rentalCreateDTO){

        Rental rental = rentalCreateDTO.toEntity(Rental.class);
        this.rentalService.create(rental, rentalCreateDTO.propertyId(), rentalCreateDTO.offerId());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(rental.getId())
            .toUri();
        
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Rental>> listAllRents(){
        List<Rental> rentals = this.rentalService.findAllByUser();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> findById(@PathVariable Long id){
        Rental rental = this.rentalService.findById(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/properties/{propertyId}")
    public ResponseEntity<List<Rental>> findByProperty_Id(@PathVariable Long propertyId){
        List<Rental> rental = this.rentalService.findByPropertyId(propertyId);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(
        @Valid @RequestBody RentalUpdateDTO rentalUpdateDTO,
        @PathVariable Long id
    ){
        this.rentalService.update(id, rentalUpdateDTO.toEntity(Rental.class));
        return ResponseEntity.noContent().build();
    }



}
