package rostem.model.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Used to store the user and determine it's authorities
 */

public class UserPrincipal implements UserDetails {
    private static final long serialVersionUid = 1L;

    private final User user;

    //
    // Compute the authorities by user type
    //
    private List<String> getAuthoritiesForUser(User user){
        List<String> authorities = new ArrayList<>();

        if(user instanceof RostemUser)
            authorities.add("ROLE_USER");
        else if(user instanceof RostemAdmin)
            authorities.add("ROLE_ADMIN");
        return authorities;
    }
    public UserPrincipal(User user) {
        this.user = user;
    }

    //
    // Returns a list of authorities for a user
    //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        getAuthoritiesForUser(this.user)
                .forEach(el->authorities.add((GrantedAuthority) () -> el));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.password;
    }

    @Override
    public String getUsername() {
        return user.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //
    //Returns user's details and role
    //
    public UserLoginDetail getUser() {
        String role = getAuthoritiesForUser(this.user).get(0);
        return new UserLoginDetail(this.user,role);
    }
}
