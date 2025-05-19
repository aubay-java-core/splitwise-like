package pt.community.java.splitwise_like.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.repository.UsersRepository;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractCrudService<Users, Long>  implements UserDetailsService, UserService {

    private final UsersRepository usersRepository;

    @Override
    protected JpaRepository<Users, Long> getRepository() {
        return usersRepository;
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        return usersRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("USER")) //TODO
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
