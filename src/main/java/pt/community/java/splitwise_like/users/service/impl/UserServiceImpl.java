package pt.community.java.splitwise_like.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.repository.UsersRepository;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UsersRepository usersRepository;

    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
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

    @Override
    public Users save(Users user) {
        return usersRepository.save(user);
    }
}
