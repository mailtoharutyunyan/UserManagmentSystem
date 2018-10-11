package am.nova.user.management.demo.service;

import am.nova.user.management.demo.model.User;
import am.nova.user.management.demo.payload.UserPayload;

import java.util.UUID;

public interface UserService {
    User create(UserPayload userPayload);
    User getUser(UUID userId);
    User update();
    void delete();

}
