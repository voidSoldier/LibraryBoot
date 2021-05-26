package my.company.libraryboot.service;

import my.company.libraryboot.error.EntityNotFoundException;
import my.company.libraryboot.model.User;
import my.company.libraryboot.model.enums.Role;
import my.company.libraryboot.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getUserById(int id, Pageable pageable) throws EntityNotFoundException {
        Page<User> result = userRepository.findUserById(id, pageable);

        if (!result.isEmpty())
            return result;
        else throw new EntityNotFoundException(String.format("User with id %d doesn't exist!", id));
    }

    public User addRole(User user, Role newRole) {
        Set<Role> roles = user.getRoles();
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    public User removeRole(User user, Role roleToRemove) {
        user.setRoles(
                user.getRoles().stream()
                        .filter(r -> r != roleToRemove)
                        .collect(Collectors.toSet()));
        userRepository.save(user);

        return user;
    }

    public void deleteUser(int id) {
        userRepository.deleteUserById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
