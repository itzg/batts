package net.itzgande.batts.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itzgande.batts.domain.BattsUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

@Service(value="userDetailsService")
public class BattsMongoUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(BattsMongoUserDetailsService.class);
	
	private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_USER");
	
	private Map<String, BattsUserDetails> registeredUsers = new HashMap<String, BattsUserDetails>();

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		logger.info("Got asked to load user {}",username);
		BattsUserDetails user = registeredUsers.get(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}

	@Override
	public UserDetails loadUserDetails(OpenIDAuthenticationToken token)
			throws UsernameNotFoundException {
        String id = token.getIdentityUrl();
        logger.info("Loading OpenID user details for {}",id);

        BattsUserDetails user = registeredUsers.get(id);
        
        if (user != null) {
        	logger.info("returning user");
            return user;
        }

        user = new BattsUserDetails(id, DEFAULT_AUTHORITIES);
        logger.info("auto registering {}", id);
        registeredUsers.put(id, user);
        
        // Using technique from spring-security openid sample to return a one-time
        // copy of the object that indicates it is a new user
        user = new BattsUserDetails(id, DEFAULT_AUTHORITIES);
        user.setNewUser(true);
        
		return user;
	}

}
