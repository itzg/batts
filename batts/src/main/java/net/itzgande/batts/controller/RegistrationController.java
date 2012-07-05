package net.itzgande.batts.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import net.itzgande.batts.config.BattsUserDetails;
import net.itzgande.batts.domain.BattsUser;
import net.itzgande.batts.domain.Household;
import net.itzgande.batts.domain.HouseholdShare;
import net.itzgande.batts.service.BattsMongoUserDetailsService;
import net.itzgande.batts.service.InitMongoService;
import net.itzgande.batts.service.SharesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	InitMongoService initMongoService;
	
	@Autowired
	BattsMongoUserDetailsService userDetailsService;
	
	@Autowired
	SharesService sharesService;

	@RequestMapping(value = "/create", method=RequestMethod.GET)
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
	
	@RequestMapping(value = "/household/api/share", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public HouseholdShare share(HttpServletRequest request, Principal user) {
		Household household = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		BattsUser battsUser = BattsUserDetails.extractFromPrincipal(user);

		HouseholdShare share = sharesService.allocate(household, battsUser);
		
		return share;
	}
	
	@RequestMapping(value = "/join", method=RequestMethod.POST)
	public String/*view*/ join(Principal user, @RequestParam int shareCode) {
		BattsUser battsUser = BattsUserDetails.extractFromPrincipal(user);

		HouseholdShare householdShare = sharesService.join(shareCode);
		if (householdShare != null) {
			userDetailsService.joinUserToHousehold(battsUser, householdShare.getHousehold());
		}
		
		return "redirect:/";
	}
}
