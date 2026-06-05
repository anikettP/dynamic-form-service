package com.crpf.forms.security;

import com.crpf.forms.entity.Role;
import com.crpf.forms.entity.User;
import com.crpf.forms.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndEnabledTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            
            // Map roles to permissions
            if ("ROLE_ADMIN".equalsIgnoreCase(role.getRoleName())) {
                authorities.add(new SimpleGrantedAuthority("CREATE"));
                authorities.add(new SimpleGrantedAuthority("UPDATE"));
                authorities.add(new SimpleGrantedAuthority("DELETE"));
                authorities.add(new SimpleGrantedAuthority("VIEW"));
            } else if ("ROLE_USER".equalsIgnoreCase(role.getRoleName())) {
                authorities.add(new SimpleGrantedAuthority("CREATE"));
                authorities.add(new SimpleGrantedAuthority("UPDATE"));
                authorities.add(new SimpleGrantedAuthority("VIEW"));
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}
