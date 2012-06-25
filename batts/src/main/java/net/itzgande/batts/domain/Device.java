package net.itzgande.batts.domain;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="devices")
public class Device {
	private ObjectId id;
	
	private String label;
	
	/**
	 * Optional
	 * Further describes this device especially in the cases where there are
	 * more than one with the same label
	 */
	private String description;
	
	private BatteryBundle needs;
	
	private BatteryBundle using;
	
	public String toString() {
		return Device.class.getSimpleName()+"[id="+id+
				", label="+label+
				", description="+description+
				", needs="+needs+
				", using="+using+
				"]";
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BatteryBundle getNeeds() {
		return needs;
	}

	public void setNeeds(BatteryBundle needs) {
		this.needs = needs;
	}

	public BatteryBundle getUsing() {
		return using;
	}

	public void setUsing(BatteryBundle using) {
		this.using = using;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	
}
