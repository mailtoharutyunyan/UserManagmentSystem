package am.nova.user.management.demo.service;

import am.nova.user.management.demo.exception.UserNotFoundException;
import am.nova.user.management.demo.model.User;
import am.nova.user.management.demo.payload.UserPayload;
import am.nova.user.management.demo.repository.UserRepository;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(UserPayload userPayload) {
        User user = fromPayloadToUser(userPayload);
        return userRepository.save(user);
    }

    @Override
    public User getUser(UUID userId) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserId(userId));
        user.orElseThrow(UserNotFoundException::new);
        return user.get();
    }

    @Override
    public User update() {
        return null;
    }

    @Override
    public void delete() {

    }

    private User fromPayloadToUser(UserPayload userPayload){

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUserName(userPayload.getUserName());
        user.setCreatedDate(new Date());
        user.setEnabled(true);
        user.setLastPasswordResetDate(new Date());
        user.setPassword(passwordEncoder.encode(userPayload.getPassword()));
        Set<String> role = new HashSet<>();
        role.add("ROLE_USER");
        user.setRole(role);
        return user;
    }
}
