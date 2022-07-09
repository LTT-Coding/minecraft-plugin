package de.ltt.server.reflaction;

public enum OwnerType {

	FIRM,
	IMMOBILIE,
	PRIVATE;
	
	
	public static OwnerType fromString(String string) {
		if(string.equals("FIRM")) {
			return OwnerType.FIRM;
		}else if(string.equals("PRIVATE")) {
			return OwnerType.PRIVATE;
		}else if(string.equals("IMMOBILIE")) {
			return OwnerType.IMMOBILIE;
		}
		return null;
	}
	
}
