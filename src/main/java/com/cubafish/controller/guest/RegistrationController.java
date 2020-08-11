package com.cubafish.controller.guest;

import com.cubafish.utils.CustomResponseBody;
import com.cubafish.dto.UserDto;
import com.cubafish.entity.UserRole;
import com.cubafish.repository.UserRepository;
import com.cubafish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(RegistrationController.REGISTRATION_PATH)
@RequiredArgsConstructor
public class RegistrationController {

    public static final String REGISTRATION_PATH = "/guest/registration";
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    public ModelAndView method() {
        return new ModelAndView("redirect:" + "registration.html");
    }

    @PostMapping()
    public CustomResponseBody create(@RequestBody UserDto userDto) {
        String status = userService.userValidation(userDto);

        if (status.equals("registration complete")) {
            userDto.setActive(true);
            userDto.setUserRole(UserRole.USER.name());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userService.create(userDto));
        }
        return new CustomResponseBody(1L,"registration status:", status, "no data", "step", 1);
    }
}

