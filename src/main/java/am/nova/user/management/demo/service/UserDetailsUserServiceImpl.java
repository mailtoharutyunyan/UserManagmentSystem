package am.nova.user.management.demo.service;

import am.nova.user.management.demo.model.AuthenticatedUser;
import am.nova.user.management.demo.model.User;
import am.nova.user.management.demo.repository.UserRepository;
import am.nova.user.management.demo.util.AuthenticatedUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s);
        if (user==null){
            throw new UsernameNotFoundException("user with this username not found");
        }
        AuthenticatedUser authenticatedUser =  AuthenticatedUserFactory.create(user);
        return authenticatedUser;
    }
}
