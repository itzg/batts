package me.itzg.batts.config;

import java.security.Principal;
import java.util.Collection;

import me.itzg.batts.domain.BattsUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class BattsUserDetails extends User {
	private static final long serialVersionUID = 1L;
	
	private BattsUser battsUser;

	public BattsUserDetails(BattsUser dbUser,
			Collection<? extends GrantedAuthority> authorities) {
		super(dbUser.getOpenId(), "UNUSED", authorities);
		this.battsUser = dbUser;
	}
	
	@Override
	public String toString() {
		return BattsUserDetails.class.getSimpleName()+":[dbUser="+battsUser+
				", super="+super.toString()+"]";
	}

	/**
	 * 
	 * @return the persisted user record
	 */
	public BattsUser getBattsUser() {
		return battsUser;
	}

	public static BattsUser extractFromPrincipal(Principal user) {
		Authentication authenticatedUser = (Authentication) user;
		BattsUserDetails battsUserDetails = (BattsUserDetails) authenticatedUser.getPrincipal();
		return battsUserDetails.getBattsUser();
	}

}
