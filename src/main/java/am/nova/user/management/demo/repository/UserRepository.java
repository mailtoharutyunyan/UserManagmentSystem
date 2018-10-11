package am.nova.user.management.demo.repository;

import am.nova.user.management.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

    public User findByUserName(String userName);

    public User findByUserId(UUID userId);
}
