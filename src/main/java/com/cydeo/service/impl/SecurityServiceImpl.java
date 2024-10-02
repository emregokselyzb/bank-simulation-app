package com.cydeo.service.impl;

import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findByUserName(username);

        if(username==null){
            throw new UsernameNotFoundException("This user not exist");
        }

        return new UserPrincipal(user);
    }
}
