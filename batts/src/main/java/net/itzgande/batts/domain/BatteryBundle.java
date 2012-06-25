package net.itzgande.batts.domain;

public class BatteryBundle {
	private String batteryTypeKey;
	
	private int count;

	public BatteryBundle() {
	}
	
	public BatteryBundle(String key, int count) {
		batteryTypeKey = key;
		this.count = count;
	}
	
	@Override
	public String toString() {
		return BatteryBundle.class.getSimpleName()+"[type="+batteryTypeKey+
				", count="+count+"]";
	}

	public String getBatteryTypeKey() {
		return batteryTypeKey;
	}

	public void setBatteryTypeKey(String batteryTypeKey) {
		this.batteryTypeKey = batteryTypeKey;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
