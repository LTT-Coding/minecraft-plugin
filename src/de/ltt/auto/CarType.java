package de.ltt.auto;

import de.ltt.auto.cars.Bike_Red;
import de.ltt.auto.cars.RTW;

public enum CarType {

	MEDIC(RTW.class), 
	POLICE(RTW.class),
	ZIVI1(RTW.class),
	REDBIKE(Bike_Red.class);
	
	@SuppressWarnings("rawtypes")
	private CarType(Class translation) {
		this.translation = translation;
	}

	@SuppressWarnings("rawtypes")
	private Class translation;

	@SuppressWarnings("rawtypes")
	public Class getTranslation() {
		return translation;
	}
}
