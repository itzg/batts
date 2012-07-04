package net.itzgande.batts.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.itzgande.batts.DeviceComparator;
import net.itzgande.batts.domain.BatteryBundle;
import net.itzgande.batts.domain.Device;
import net.itzgande.batts.domain.Household;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import com.mongodb.WriteResult;

@Controller
@RequestMapping("/household/api")
public class HouseholdApiController {
	private static final Logger logger = LoggerFactory
			.getLogger(HouseholdApiController.class);
	
	public static class Counts {
		public String batteryTypeKey;
		public int available;
		public int inuse;
	}	
	
	public static class DeviceTransferResults {
		public int available;
		public int totalInUse;
		public int inDevice;
	}
	
	private static final DeviceComparator DEVICE_COMPARATOR = new DeviceComparator();
	
	@Autowired
	MongoTemplate mongoTemplate;

	@RequestMapping(value="available", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<BatteryBundle> findAvailableBundles(HttpServletRequest request) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("available");
		Household household = mongoTemplate.findOne(query, Household.class);
		return household.getAvailable();
	}
	
	/**
	 * 
	 * @param count
	 * @return the new available count
	 */
	@RequestMapping(value="incAvailable/{batteryTypeKey}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public int incAvailable(HttpServletRequest request, @PathVariable String batteryTypeKey, @RequestParam int count) {
		logger.info("Incrementing available "+batteryTypeKey+" by "+count);
		return adjustAvailable(request, batteryTypeKey, count);
	}
	
	/**
	 * 
	 * @param count
	 * @return the new available count
	 */
	@RequestMapping(value="decAvailable/{batteryTypeKey}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public int decAvailable(HttpServletRequest request, @PathVariable String batteryTypeKey, @RequestParam int count) {
		logger.info("Decrementing available "+batteryTypeKey+" by "+count);
		return adjustAvailable(request, batteryTypeKey, -count);
	}
	
	
	/**
	 * 
	 * @param count
	 * @return the new available count
	 */
	@RequestMapping(value="adjustAvailable/{batteryTypeKey}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public int adjustAvailable(HttpServletRequest request, @PathVariable String batteryTypeKey, @RequestParam int count) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		final Query query = new Query(where("_id").is(householdToFind.getId()).and("available.batteryTypeKey").is(batteryTypeKey));
		query.fields().include("available");
		Household result = mongoTemplate.findAndModify(query, 
				new Update().inc("available.$.count", count),
				new FindAndModifyOptions().returnNew(true),
				Household.class);
		for (BatteryBundle bb : result.getAvailable()) {
			if (bb.getBatteryTypeKey().equals(batteryTypeKey)) {
				return bb.getCount();
			}
		}
		logger.error("Updated but battery type didn't match result extraction for "+batteryTypeKey);
		return 0;
	}
	
	@RequestMapping(value="counts", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Counts> findBundleCounts(HttpServletRequest request) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("available").include("devices");
		Household household = mongoTemplate.findOne(query, Household.class);
		
		List<Counts> results = new ArrayList<Counts>();
		for (BatteryBundle b : household.getAvailable()) {
			final Counts c = new Counts();
			c.batteryTypeKey = b.getBatteryTypeKey();
			c.available = b.getCount();
			if (household.getDevices() != null) {
				// sloppy, but simple
				for (Device d : household.getDevices()) {
					if (d.getUsing().getBatteryTypeKey()
							.equals(c.batteryTypeKey)) {
						c.inuse += d.getUsing().getCount();
					}
				}
			}
			results.add(c);
		}
		
		return results;
	}
	
	@RequestMapping(value="addDevice", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public List<Device> addDevice(HttpServletRequest request, 
			@RequestParam String label, 
			@RequestParam(value="description",required=false) String description, 
			@RequestParam int count, @RequestParam String type) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		Device device = new Device();
		device.setId(ObjectId.get().toString());
		device.setLabel(label);
		device.setDescription(description);
		device.setNeeds(new BatteryBundle(type, count));
		device.setUsing(new BatteryBundle(type, 0));
		logger.info("Adding device "+device);
		
		final Query query = new Query(where("_id").is(householdToFind.getId()));
		query.fields().include("devices");
		Household result = mongoTemplate.findAndModify(query, 
				new Update().push("devices", device),
				new FindAndModifyOptions().returnNew(true),
				Household.class);

		return sort(result.getDevices());
	}
	
	
	@RequestMapping(value="editDevice", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public List<Device> editDevice(HttpServletRequest request, @RequestParam String deviceId,
			@RequestParam String label, 
			@RequestParam(value="description",required=false) String description) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("devices");
		Household result = mongoTemplate.findOne(query, Household.class);
		if (result != null && result.getDevices() != null) {
			for (Device d : result.getDevices()) {
				if (d.getId().equals(deviceId)) {
					d.setLabel(label);
					d.setDescription(description);
				}
			}
			
			Household committedResult = mongoTemplate.findAndModify(query, update("devices", result.getDevices()), 
					options().returnNew(true), Household.class);
			
			return sort(committedResult.getDevices());
		}
		return null;
	}
	
	@RequestMapping(value="devices", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Device> getDevices(HttpServletRequest request) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("devices");
		Household household = mongoTemplate.findOne(query, Household.class);
		return sort(household.getDevices());
	}
	
	@RequestMapping(value="putInDevice", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public DeviceTransferResults putBatteriesInDevice(HttpServletRequest request, @RequestParam String batteryTypeKey, @RequestParam String deviceId,
			@RequestParam int count) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);

		// Error checking?
		mongoTemplate.updateFirst(new Query(where("_id").is(householdToFind.getId())
				.and("available.batteryTypeKey").is(batteryTypeKey)
				), 
				new Update().inc("available.$.count", -count),
				Household.class);
		final Query query = new Query(where("_id").is(householdToFind.getId())
				.and("devices._id").is(new ObjectId(deviceId))
				);
		query.fields().include("available").include("devices");
		Household queryResults = mongoTemplate.findAndModify(query, 
				new Update().inc("devices.$.using.count", count),
				new FindAndModifyOptions().returnNew(true),
				Household.class);
		
		if (queryResults != null) {
			DeviceTransferResults results = new DeviceTransferResults();
			Iterator<BatteryBundle> availableIterator = queryResults.getAvailable()
					.iterator();
			while (availableIterator.hasNext()) {
				BatteryBundle bb = availableIterator.next();
				if (bb.getBatteryTypeKey().equals(batteryTypeKey)) {
					results.available = bb.getCount();
					break;
				}
			}
			Iterator<Device> devicesIterator = queryResults.getDevices().iterator();
			while (devicesIterator.hasNext()) {
				Device d = devicesIterator.next();
				if (d.getUsing().getBatteryTypeKey().equals(batteryTypeKey)) {
					results.totalInUse += d.getUsing().getCount();
				}
				if (d.getId().equals(deviceId)) {
					results.inDevice = d.getUsing().getCount();
					// don't break so that we can populate the totalInUse
				}
			}
			return results;
		}
		else {
			throw new HttpServerErrorException(HttpStatus.NOT_MODIFIED);
		}
	}
	
	@RequestMapping(value="deleteDevice", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public List<Device> deleteDevice(HttpServletRequest request, @RequestParam String id) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		Query query = new Query(where("_id").is(householdToFind.getId()));
		query.fields().include("devices");
		Household result = mongoTemplate.findOne(query, Household.class);
		if (result != null) {
			if (result.getDevices() != null) {
				Iterator<Device> it = result.getDevices().iterator();
				while (it.hasNext()) {
					if (it.next().getId().equals(id)) {
						it.remove();
					}
				}
				Household committedResult = mongoTemplate.findAndModify(query, 
						new Update().set("devices", result.getDevices()), 
						new FindAndModifyOptions().returnNew(true), Household.class);
				if (committedResult != null) {
					return sort(committedResult.getDevices());
				}
			}
		}
		return null;
	}

	private static List<Device> sort(List<Device> devices) {
		if (devices != null) {
			Collections.sort(devices, DEVICE_COMPARATOR);
		}
		return devices;
	}
}
