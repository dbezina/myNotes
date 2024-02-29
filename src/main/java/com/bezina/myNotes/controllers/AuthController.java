package com.bezina.myNotes.controllers;

import com.bezina.myNotes.security.JWTTokenProvider;
import com.bezina.myNotes.security.SecurityConstants;
import com.bezina.myNotes.services.UserService;
import com.bezina.myNotes.payload.request.LoginRequest;
import com.bezina.myNotes.payload.request.SignupRequest;
import com.bezina.myNotes.payload.response.JWTTokenSuccessResponse;
import com.bezina.myNotes.payload.response.MessageResponse;
import com.bezina.myNotes.validators.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    public static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        LOG.info("authenticateUser");
        ResponseEntity<Object> responseErrors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(responseErrors)) {
            LOG.error("authenticateUser has errors" + responseErrors.getStatusCode().toString());
            return responseErrors;
        }

        LOG.info(loginRequest.getUsername() + loginRequest.getPassword());
        //    Authentication authentication = authenticationManager.authenticate(
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationToken;
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.getToken(authentication);
        //   LOG.info("token = "+jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult result) {
        ResponseEntity<Object> responseErrors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(responseErrors)) {
            LOG.info("registerUser method has Errors");
            for (ObjectError error : result.getAllErrors()) {
                LOG.error(error.toString() + "/n");
            }
            LOG.error(responseErrors.toString());
            LOG.error(responseErrors.getStatusCode().toString());

            return responseErrors;
        }

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
