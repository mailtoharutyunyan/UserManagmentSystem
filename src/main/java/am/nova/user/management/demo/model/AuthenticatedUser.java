package am.nova.user.management.demo.model;

import am.nova.user.management.demo.util.AuthenticatedUserFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class AuthenticatedUser implements UserDetails {

    private UUID userId;

    private String userName;

    private String password;

    private boolean enabled;

    private Date createdDate;

    private Date lastPasswordResetDate;

    private Collection<GrantedAuthority> authorities;

    public AuthenticatedUser(){
    }

    public AuthenticatedUser(UUID userId, String userName, String password, boolean enabled,
                             Date createdDate, Date lastPasswordResetDate, Collection<GrantedAuthority> authorities){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.createdDate = createdDate;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    public UUID getUserId(){
        return userId;
    }

    public Date getCreatedDate(){
        return createdDate;
    }

    public Date getLastPasswordResetDate(){
        return lastPasswordResetDate;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return userName;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return false;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }

    public void setUserId(UUID userId){
        this.userId = userId;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void setCreatedDate(Date createdDate){
        this.createdDate = createdDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate){
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities){
        this.authorities = authorities;
    }

    public void setAuthorities(Set<String> authorities){
        AuthenticatedUserFactory.mapToGrantedAuthorities(authorities);
    }
}
