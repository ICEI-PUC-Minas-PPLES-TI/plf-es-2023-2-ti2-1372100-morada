package br.pucminas.morada.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.pucminas.morada.models.property.Property;
import br.pucminas.morada.models.user.User;
import br.pucminas.morada.models.user.UserRole;
import br.pucminas.morada.models.visit.Visit;
import br.pucminas.morada.repositories.UserRepository;
import br.pucminas.morada.repositories.VisitRepository;
import br.pucminas.morada.security.UserSpringSecurity;
import br.pucminas.morada.services.exceptions.AuthorizationException;
import br.pucminas.morada.services.exceptions.GenericException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


//* service - camada de negócios: favorece a reusabilidade

@Service
public class VisitService {
    
    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserService userService;

    public Visit findById(Long id){
        Optional<Visit> optionalVisit = this.visitRepository.findById(id);
        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        
        if (optionalVisit.isEmpty()) {
            if (userSpringSecurity != null && userSpringSecurity.hasRole(UserRole.ADMIN)) {
                throw new GenericException(HttpStatus.NOT_FOUND, "Visita não encontrada.");
            }
        } else {
            Visit visit = optionalVisit.get();
            if (userSpringSecurity != null && (userSpringSecurity.hasRole(UserRole.ADMIN) || visit.getUser().getId().equals(userSpringSecurity.getId()))) {
                return visit;
            }
        }
        throw new AuthorizationException();
    }
    
    public List<Visit> findAllByUser() {

        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        return this.visitRepository.findByUser_Id(userSpringSecurity.getId());

    }

    public List<Visit> findAllVisitsInProperty(){        
        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        if(userSpringSecurity != null)
            return this.visitRepository.findAllVisitsInProperty(userSpringSecurity.getId());
        else
            throw new AuthorizationException();
    }

    public List<Visit> findAll(Specification<Visit> specification) {

        return this.visitRepository.findAll(specification);

    }

    @Transactional
    public Visit create(Visit visit){
        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        User user = this.userService.findById(userSpringSecurity.getId());
        visit.setId(null);
        visit.setUser(user);
        visit = this.visitRepository.save(visit);
        return visit;
    }

    @Transactional
    public Visit update(Visit visit){
        Visit newVisit = findById(visit.getId());
        newVisit.setDatetime(visit.getDatetime());
        newVisit.setCariedOut(visit.getCariedOut());

        return this.visitRepository.save(newVisit);
    }

    @Transactional
    public void delete(Long id){
        findById(id);
        this.visitRepository.deleteById(id);
    }
    
}