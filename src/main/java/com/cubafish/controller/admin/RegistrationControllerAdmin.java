package com.cubafish.controller.admin;

import com.cubafish.dto.UserDto;
import com.cubafish.entity.UserRole;
import com.cubafish.repository.UserRepository;
import com.cubafish.service.UserService;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(RegistrationControllerAdmin.REGISTRATION_PATH)
@RequiredArgsConstructor
public class RegistrationControllerAdmin {

    public static final String REGISTRATION_PATH = "/admin/registration";
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    public ModelAndView method() {
        return new ModelAndView("redirect:" + "registration.html");
    }

    @PostMapping()
    public CustomResponseBody create(@RequestBody UserDto userDto) {
        String status;
        if (!userService.existsByRole(UserRole.ADMIN.name())) {
            status = userService.userValidation(userDto);
        } else {
            status = "The administrator is already registered";
        }

        if (status.equals("validation complete")) {
            userDto.setActive(true);
            userDto.setUserRole(UserRole.ADMIN.name());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userService.create(userDto));
            status = "registration complete";
        }
        return new CustomResponseBody(1L, "registration status:", status, "no data");
    }
}

