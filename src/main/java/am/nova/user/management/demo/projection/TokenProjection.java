package am.nova.user.management.demo.projection;

import java.util.Date;
import java.util.UUID;

public interface TokenProjection {

     UUID getUserId();

     String getAccessToken();

     String getRefreshToken();

     Date getExpireDate();
}
