package am.nova.user.management.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "token")
public class Token {

    @Id
    private UUID tokenId;

    @Indexed
    private UUID userId;

    private String accessToken;

    private String refreshToken;

    private Date expireDate;

}
