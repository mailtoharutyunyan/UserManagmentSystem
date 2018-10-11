package am.nova.user.management.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Indexed
    private UUID userId;

    @Indexed
    private String userName;

    private String password;

    private boolean enabled;

    private Date createdDate;

    private Date lastPasswordResetDate;

    private Set<String> role;
}
