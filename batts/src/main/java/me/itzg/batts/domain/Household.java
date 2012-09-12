package me.itzg.batts.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="households")
public class Household implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ATTRIBUTE_NAME = Household.class.getCanonicalName();

	private String id;
	
	private List<BatteryBundle> available;

	private List<Device> devices;
	
	@Override
	public String toString() {
		return Household.class.getSimpleName()+"[id="+id+"]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BatteryBundle> getAvailable() {
		return available;
	}

	public void setAvailable(List<BatteryBundle> available) {
		this.available = available;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
}
