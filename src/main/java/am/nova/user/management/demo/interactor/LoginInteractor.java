package am.nova.user.management.demo.interactor;

import am.nova.user.management.demo.model.AuthenticatedUser;
import am.nova.user.management.demo.model.Token;
import am.nova.user.management.demo.payload.LoginRequest;
import am.nova.user.management.demo.service.TokenService;
import am.nova.user.management.demo.service.UserService;
import am.nova.user.management.demo.util.JwtTokenUtil;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.UUID;

public class LoginInteractor {

    private TokenService tokenService;

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;

    public LoginInteractor(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService){
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    public Token getToken(LoginRequest loginRequest, Device device){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.userName,
                        loginRequest.password
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(loginRequest.userName);
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String accessToken = jwtTokenUtil.generateToken(authenticatedUser,device);
        String refreshToken = jwtTokenUtil.generateRefreshToken(authenticatedUser, device);
        Date expireDate = jwtTokenUtil.getExpirationDateFromToken(accessToken);
        Token token = tokenService.createToken(new Token(UUID.randomUUID(), authenticatedUser.getUserId(), accessToken, refreshToken,expireDate ));
        return token;
    }
}
