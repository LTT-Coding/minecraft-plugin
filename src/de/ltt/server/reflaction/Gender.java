package de.ltt.server.reflaction;

public enum Gender {

	MALE("M�nnlich"),
	FEMALE("Weiblich"),
	DIVERSE("Divers");
	
	public String translation;
	private Gender(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}
}
