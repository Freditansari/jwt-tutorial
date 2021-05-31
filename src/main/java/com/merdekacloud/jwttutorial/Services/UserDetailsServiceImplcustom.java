package com.merdekacloud.jwttutorial.Services;

import com.merdekacloud.jwttutorial.Models.User;
import com.merdekacloud.jwttutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("UserDetailsServiceImplcustom")
public class UserDetailsServiceImplcustom implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found "+ username));
        return UserPrinciple.build(user);
    }

    public UserDetails loadUserById(Long id){
        User user = userRepository.getById(id);
        return UserPrinciple.build(user);
    }

}
