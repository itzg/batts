package net.itzgande.batts.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BattsUserDetails extends User {
	private static final long serialVersionUID = 1L;
	
	private boolean newUser;

	public BattsUserDetails(String username,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, "UNUSED", authorities);
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}


}
