package ru.filit.mdma.service;

import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

  @Override
  public String getAuthName() {
    return getAuthentication().getName();
  }

  @Override
  public String getAuthRole() {
    Object principal = getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal)
          .getAuthorities();
      if (!authorities.isEmpty()) {
        Optional<? extends GrantedAuthority> role = authorities.stream()
            .filter(grantedAuthority -> grantedAuthority.getAuthority().startsWith("ROLE_"))
            .findFirst();
        if (role.isPresent()) {
          return role.get().getAuthority().substring(5);
        }
      }
    }
    return null;
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}