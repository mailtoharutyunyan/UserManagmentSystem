package am.nova.user.management.demo.controller;

import am.nova.user.management.demo.interactor.LoginInteractor;
import am.nova.user.management.demo.payload.LoginRequest;
import am.nova.user.management.demo.projection.TokenProjection;
import am.nova.user.management.demo.service.TokenService;
import am.nova.user.management.demo.service.UserService;
import am.nova.user.management.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ProjectionFactory projectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/oauth/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenProjection createToken(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        LoginInteractor loginInteractor = new LoginInteractor(tokenService, userService, authenticationManager, jwtTokenUtil, userDetailsService);
        LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
        return projectionFactory.createProjection(TokenProjection.class, loginInteractor.getToken(loginRequest,deviceResolver.resolveDevice(request)));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<UserController.ErrorMessage> userCredentialsExpired(CredentialsExpiredException e){
        UserController.ErrorMessage errorMessage = new UserController.ErrorMessage();
        errorMessage.status= "asasfd";
        errorMessage.message = e.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
