package net.itzgande.batts.controller;

import java.security.Principal;

import net.itzgande.batts.config.BattsUserDetails;
import net.itzgande.batts.domain.BattsUser;
import net.itzgande.batts.service.BattsMongoUserDetailsService;
import net.itzgande.batts.service.InitMongoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class RegistrationController {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	InitMongoService initMongoService;
	
	@Autowired
	BattsMongoUserDetailsService userDetailsService;

	@RequestMapping(value = "create", method=RequestMethod.GET)
	public String create(Principal user) {
		BattsUser battsUser = BattsUserDetails.extractFromPrincipal(user);
		if (battsUser.getHousehold() == null) {
			battsUser.setHousehold(initMongoService.createHousehold());
			userDetailsService.save(battsUser);
		}
		else {
			logger.info("The user {} already had a household, so not creating another", battsUser);
		}
		
		return "redirect:/";
	}
}
