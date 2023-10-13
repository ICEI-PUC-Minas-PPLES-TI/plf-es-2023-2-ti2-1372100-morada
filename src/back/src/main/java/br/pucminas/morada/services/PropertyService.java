package br.pucminas.morada.services;

import br.pucminas.morada.models.property.Property;
import br.pucminas.morada.models.property.PropertyStatus;
import br.pucminas.morada.models.user.User;
import br.pucminas.morada.models.user.UserRole;
import br.pucminas.morada.repositories.PropertyRepository;
import br.pucminas.morada.security.UserSpringSecurity;
import br.pucminas.morada.services.exceptions.AuthorizationException;
import br.pucminas.morada.services.exceptions.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Property create(Property property) {

        User user = this.userService.findById(UserService.authenticated().getId());

        property.setId(null);
        property.setUser(user);

        return this.propertyRepository.save(property);

    }

    @Transactional
    public Property update(Property property) {

        Property propertyFound = this.findById(property.getId());
        propertyFound.setStatus(property.getStatus());

        return this.propertyRepository.save(propertyFound);

    }

    public Property findById(Long id) {

        Optional<Property> optionalProperty = this.propertyRepository.findById(id);
        UserSpringSecurity userSpringSecurity = UserService.authenticated();

        if (optionalProperty.isEmpty()) {

            System.out.println("Debug 1");

            if (userSpringSecurity != null && userSpringSecurity.hasRole(UserRole.ADMIN)) {
                System.out.println("Debug 2");
                throw new GenericException(HttpStatus.NOT_FOUND, "Propriedade não encontrada.");
            }

        } else {

            Property property = optionalProperty.get();

            if (property.getStatus() == PropertyStatus.APPROVED
                    || userSpringSecurity != null && (userSpringSecurity.hasRole(UserRole.ADMIN) || property.getUser().getId().equals(userSpringSecurity.getId()))) {
                System.out.println("Debug 3");
                return property;
            }

        }

        System.out.println("Debug 4");
        throw new AuthorizationException("Acesso negado.");

    }

    public List<Property> findAllByUser() {

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        return this.propertyRepository.findByUserId(userSpringSecurity.getId());

    }

    public List<Property> findAll(Specification<Property> specification) {

        return this.propertyRepository.findAll(specification);

    }

}
