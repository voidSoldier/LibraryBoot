package my.company.libraryboot.auth;

import lombok.extern.slf4j.Slf4j;
import my.company.libraryboot.model.User;
import my.company.libraryboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("authenticating '{}'", username);
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(username);
        return new AuthUser(optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User '" + username + "' was not found")));
    }
}
