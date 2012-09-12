package me.itzg.batts.controller;

import java.security.Principal;
import java.util.Locale;

import me.itzg.batts.config.BattsUserDetails;
import me.itzg.batts.domain.BattsUser;
import me.itzg.batts.service.UsageTrackingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	UsageTrackingService usageTrackingService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, Principal user) {
		BattsUser battsUser = BattsUserDetails.extractFromPrincipal(user);
		logger.debug("User {} entered home location", battsUser);
		model.addAttribute("battsUser", battsUser);
		if (battsUser.getHousehold() == null) {
			return "welcome";
		}
		else {
			usageTrackingService.accessed(battsUser);
			return "batts";
		}
	}
}
