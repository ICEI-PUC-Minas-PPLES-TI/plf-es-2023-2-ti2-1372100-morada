package br.pucminas.morada.services;

import br.pucminas.morada.models.user.User;
import br.pucminas.morada.models.user_verification.UserVerification;
import br.pucminas.morada.models.user_verification.UserVerificationStatus;
import br.pucminas.morada.models.user.UserRole;
import br.pucminas.morada.repositories.UserVerificationRepository;
import br.pucminas.morada.security.UserSpringSecurity;
import br.pucminas.morada.services.exceptions.AuthorizationException;
import br.pucminas.morada.services.exceptions.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserVerificationService {

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public UserVerification create(UserVerification userVerification) {

        User user = this.userService.findById(UserService.getAuthenticatedUser().getId());

        userVerification.setId(null);
        userVerification.setUser(user);
        userVerification.setStatus(UserVerificationStatus.PENDING_APPROVAL);

        return this.userVerificationRepository.save(userVerification);
    }

    @Transactional
    public UserVerification update(Long id, UserVerification userVerification) {

        UserVerification userVerificationFound = this.findById(id);

        userVerificationFound.setAdminMessage(userVerification.getAdminMessage());
        userVerificationFound.setStatus(userVerification.getStatus());

        return this.userVerificationRepository.save(userVerificationFound);

    }

    public UserVerification findById(Long id) {

        Optional<UserVerification> optionalUserVerification = this.userVerificationRepository.findById(id);
        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();

        if (optionalUserVerification.isEmpty()) {

            if (userSpringSecurity.hasRole(UserRole.ADMIN)) {
                throw new GenericException(HttpStatus.NOT_FOUND, "Verificação de usuário não encontrada.");
            }

            throw new AuthorizationException();

        } else {

            UserVerification userVerification = optionalUserVerification.get();

            if (!userSpringSecurity.hasRole(UserRole.ADMIN) && !userVerification.getUser().getId().equals(userSpringSecurity.getId())) {
                throw new AuthorizationException();
            }

            return userVerification;

        }

    }

    public List<UserVerification> findAllByUser() {

        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();
        return this.userVerificationRepository.findByUserId(userSpringSecurity.getId());

    }

    public List<UserVerification> findAll() {

        UserSpringSecurity userSpringSecurity = UserService.getAuthenticatedUser();

        if (userSpringSecurity != null && (userSpringSecurity.hasRole(UserRole.ADMIN))) {
            return this.userVerificationRepository.findAllPending();
        }else{
            throw new AuthorizationException();
        }
    }

}
