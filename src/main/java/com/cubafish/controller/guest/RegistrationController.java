package com.cubafish.controller.guest;

import com.cubafish.utils.CustomResponseBody;
import com.cubafish.dto.UserDto;
import com.cubafish.entity.UserRole;
import com.cubafish.repository.UserRepository;
import com.cubafish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RegistrationController.REGISTRATION_PATH)
@RequiredArgsConstructor
public class RegistrationController {

    private static final Logger log = Logger.getLogger(RegistrationController.class);
    public static final String REGISTRATION_PATH = "/guest/registration";

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping()
    public CustomResponseBody create(@RequestBody UserDto userDto) {
        String status = userService.userValidation(userDto);

        if (status.equals("validation complete")) {
            userDto.setActive(true);
            userDto.setUserRole(UserRole.USER.name());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userService.create(userDto));
            log.info("user: " + userDto.getUsername() + " is successfully registered");
        } else {
            log.error("user: " + userDto.getUsername() + " could not to register " + "status: " + status);
        }
        return new CustomResponseBody(1L, "registration status:", status, "no data");
    }
}

