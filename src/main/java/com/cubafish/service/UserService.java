package com.cubafish.service;

import com.cubafish.dto.UserDto;
import com.cubafish.entity.User;
import com.cubafish.mapper.UserMapper;
import com.cubafish.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findAll() {
        return userMapper.mapEntitiesToDtos(userRepository.findAll());
    }

    public Boolean existsByName(String name) {
        return userRepository.existsByUsername(name);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByUserContact(String contact) {
        return userRepository.existsByUserContact(contact);
    }

    public User create(UserDto userDto) {
        return userMapper.mapDtoToEntity(userDto);
    }

    public String userValidation(UserDto userDto) {
        // checking user name
        String userName = userDto.getUsername();
        if (userName == null) {
            return "field <<user name>> is empty, enter a user name";
        } else if (userName.length() > 31) {
            return "your name have more than 30 characters";
        } else if (userName.length() < 2) {
            return "your name have less than 2 characters";
        } else if (existsByName(userName)) {
            return "User with name: <<" + userName + ">> is exists";
        } else {
            char[] name = userName.toCharArray();
            char[] checkName = {'!', '?', '@', '%', '$', '&', '*', '(', ')', '+', '<', '>', ',', '.', ':'};
            for (char nameCh : name) {
                for (char checkCh : checkName) {
                    if (nameCh == checkCh) {
                        return "user name can't have <<" + checkCh + ">>";
                    }
                }
            }
        }

        // checking password
        String userPassword = userDto.getPassword();
        if (userPassword == null) {
            return "field <<user password>> is empty, enter a password";
        } else if (userPassword.length() > 16) {
            return "your password have more than 15 characters";
        } else if (userPassword.length() < 2) {
            return "your password have less than 2 characters";
        } else {
            char[] password = userPassword.toCharArray();
            char[] checkPassword = {'!', '?', '@', '%', '$', '&', '*', '(', ')', '+', '<', '>', ',', '.', ':'};
            for (char passwordCh : password) {
                for (char checkPasswordCh : checkPassword) {
                    if (passwordCh == checkPasswordCh) {
                        return "user password can't have <<" + checkPasswordCh + ">>";
                    }
                }
            }
        }

        //checking email
        String userEmail = userDto.getEmail();
        if (userEmail == null) {
            return "field <<user email>> is empty, enter your email";
        } else if (userEmail.length() > 51) {
            return "your email have more than 50 characters";
        } else if (userEmail.length() < 2) {
            return "your email have less than 2 characters";
        } else if (existsByEmail(userEmail)) {
            return "User with email: <<" + userEmail + ">> is exists";
        } else {
            if (!userEmail.contains("@")) {
                return "your email should have: @";
            }
        }

        //checking contact
        String userTelephone = userDto.getUserContact();
        if (userTelephone == null) {
            return "field \"telephone number\" is empty, enter your telephone";
        } else if (userTelephone.length() > 21) {
            return "your telephone have more than 20 characters";
        } else if (userTelephone.length() < 2) {
            return "your telephone have less than 2 characters";
        } else if (existsByUserContact(userTelephone)) {
            return "User with telephone number: <<" + userTelephone + ">> is exists";
        } else {
            if (!userTelephone.contains("+")) {
                return "your telephone number should start with: +";
            }
        }
        return "registration complete";
    }
}
