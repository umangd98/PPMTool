package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.Repositories.UserRepository;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.UserNameAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser)
    {
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (Custom Exception)
            newUser.setUsername(newUser.getUsername());
            //password == confirm password
            //We don't persist confirm password
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }
        catch (Exception e)
        {
         throw new UserNameAlreadyExists("Username " + newUser.getUsername() + " already exists");
        }

    }
}
