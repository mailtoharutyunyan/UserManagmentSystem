package am.nova.user.management.demo.repository;

import am.nova.user.management.demo.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TokenRepository extends MongoRepository<Token, UUID> {
}
