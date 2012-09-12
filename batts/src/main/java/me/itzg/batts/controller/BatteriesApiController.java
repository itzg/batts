package me.itzg.batts.controller;

import java.util.Map;

import me.itzg.batts.domain.BatteryTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/battery/api")
public class BatteriesApiController {

	@Autowired
	BatteryTypes batteryTypes;
	
	@RequestMapping(value="types", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, BatteryTypes.Type> getTypesAsMap() {
		return batteryTypes.asMap();
	}
}
