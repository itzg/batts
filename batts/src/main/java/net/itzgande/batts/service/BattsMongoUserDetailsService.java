package net.itzgande.batts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itzgande.batts.config.BattsUserDetails;
import net.itzgande.batts.domain.BattsUser;
import net.itzgande.batts.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(BattsMongoUserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;
	
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

        BattsUser dbUser = userRepository.findByOpenId(id);
        if (dbUser == null) {
        	dbUser = new BattsUser(id);
        	userRepository.save(dbUser);
        	logger.debug("Saved {}", dbUser);
        }
        else {
        	logger.debug("Found existing user {}", dbUser);
        }
        
        return new BattsUserDetails(dbUser, DEFAULT_AUTHORITIES);
	}

	public void save(BattsUser battsUser) {
		userRepository.save(battsUser);
	}

}
