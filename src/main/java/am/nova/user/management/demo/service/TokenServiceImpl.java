package am.nova.user.management.demo.service;


import am.nova.user.management.demo.model.Token;
import am.nova.user.management.demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token createToken(Token token) {
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }
}
