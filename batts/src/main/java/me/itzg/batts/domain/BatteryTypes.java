package me.itzg.batts.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import me.itzg.batts.domain.BatteryTypes.Type;

@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class BatteryTypes {
	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Type {
		public Type() {
		}
		
		@XmlAttribute(required=true)
		private String key;
		
		@XmlAttribute(required=true)
		private String label;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	// outer class
	
	@XmlElement(name="type")
	private List<Type> types;
	private HashMap<String, Type> mapped;
	
	public BatteryTypes() {
	}
	
	public List<Type> getTypes() {
		return types;
	}



	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public Map<String, Type> asMap() {
		if (mapped == null) {
			mapped = new HashMap<String, Type>();
			for (Type type : types) {
				mapped.put(type.getKey(), type);
			}
		}
		return mapped;
	}
}
