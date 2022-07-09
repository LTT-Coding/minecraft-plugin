package de.ltt.türSysteme;

public enum DirectionType {
	
	X,
	Z;
	
	public static DirectionType fromString(String direction) {
		if(direction.equalsIgnoreCase("x"))return X;
		if(direction.equalsIgnoreCase("z"))return Z;
		return null;
	}

}
