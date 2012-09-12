package me.itzg.batts;

import java.util.Comparator;

import me.itzg.batts.domain.Device;

public class DeviceComparator implements Comparator<Device> {

	@Override
	public int compare(Device o1, Device o2) {
		return o1.getLabel().compareToIgnoreCase(o2.getLabel());
	}

}
