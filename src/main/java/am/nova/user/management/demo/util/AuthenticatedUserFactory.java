package am.nova.user.management.demo.util;

import am.nova.user.management.demo.model.AuthenticatedUser;
import am.nova.user.management.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticatedUserFactory {

    public static AuthenticatedUser create(User user)  {
        return new AuthenticatedUser(
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                user.getCreatedDate(),
                user.getLastPasswordResetDate(),
                mapToGrantedAuthorities(user.getRole())
        );

    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(Set<String> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }
}
