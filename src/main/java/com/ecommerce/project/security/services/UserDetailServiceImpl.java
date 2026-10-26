package com.ecommerce.project.security.services;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional //entire function is called other wise it is rolled back if error occurs
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found : " + username));
        //we have this build method in userDetails implementation to make our own cutom user and then convert it to the type that userDetails needed by Spring Security
        return UserDetailsImpl.build(user);
    }
}
