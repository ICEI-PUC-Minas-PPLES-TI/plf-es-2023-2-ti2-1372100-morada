package br.pucminas.morada.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.pucminas.morada.models.visit.Visit;
import br.pucminas.morada.models.visit.dto.VisitCreateDTO;
import br.pucminas.morada.models.visit.dto.VisitUpdateDTO;
import br.pucminas.morada.services.VisitService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/visits")
@Validated
public class VisitController {

    @Autowired
    private VisitService visitService;

    //Consultar uma visita pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<Visit> findById(@PathVariable Long id) {
        Visit visit = this.visitService.findById(id);
        return ResponseEntity.ok(visit);
    }

    //Consultar visitas agendadas em todos os imóveis de usuário o qual encontra-se logado.
    //todo: ajustar todo o corpo, pois aqui está para uma unica propriedade
    @GetMapping("/owner") 
    public ResponseEntity<List<Visit>> findAllVisitsInProperty(){ //todo: in properties  //todo: ajustar todo o corpo, pois aqui está para uma unica propriedade
        List<Visit> visits = this.visitService.findAllVisitsInProperty();
        return ResponseEntity.ok(visits);
    }

    //Consultar visitas agendadas pelo usuário logado
    //todo: ajustar o corpo e lógica abaixo
    @GetMapping("/renter")
    public ResponseEntity<List<Visit>> findAllByUser(){
        List<Visit> visits = this.visitService.findAllByUser();
        return ResponseEntity.ok(visits);
    }

    //Criar uma nova visita. (no imóvel específico)
    //todo: verificar lógica corpo: id usuário e id property
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody VisitCreateDTO visitCreateDTO) {

        Visit visit = visitCreateDTO.toEntity(Visit.class);
        Visit newVisit = this.visitService.create(visit);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newVisit.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }


    //Para cancelamento de visita ou para o incremento de avaliações sobre a visita
    //todo: verificar logica de update em VisitService e no VisitUpdateDTO
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody VisitUpdateDTO visitUpdateDTO, @PathVariable Long id) {
        this.visitService.update(id, visitUpdateDTO.toEntity(Visit.class));
        return ResponseEntity.noContent().build();
    }
    //Pois há diversos tipos de update: somente da data ou somente das avaliações.
    //todo: ^^ verificar necessidade de dois métodos 

}
