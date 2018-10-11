package am.nova.user.management.demo.projection;

import java.util.Date;

public interface UserProjection {

     String getUserId();

     String getUserName();

     boolean isEnabled();

     Date getCreatedDate();
}
