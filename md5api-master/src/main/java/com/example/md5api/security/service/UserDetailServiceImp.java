package com.example.md5api.security.service;

import com.example.md5api.model.TheUser;
import com.example.md5api.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TheUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " Not found"));
        return UserDetailImp.build(user);
    }
}
