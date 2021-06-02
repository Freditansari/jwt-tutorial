package com.merdekacloud.jwttutorial.Services;

import com.merdekacloud.jwttutorial.Models.User;
import com.merdekacloud.jwttutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found "+ username));
        return UserPrinciple.build(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id){
        User user = userRepository.getById(id);
        return UserPrinciple.build(user);
    }

}
