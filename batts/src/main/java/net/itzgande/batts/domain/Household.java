package net.itzgande.batts.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="households")
public class Household {
	public static final String ATTRIBUTE_NAME = Household.class.getCanonicalName();

	private String id;
	
	private List<BatteryBundle> available;
	
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
}
