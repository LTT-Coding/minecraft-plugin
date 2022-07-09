package de.ltt.server.reflaction;

public enum FirmTypes {
	
	IMMOBILIENBÜRO("Immobilienbüro"),
	EINZELHANDEL("Einzelhandel"),
	BERGBAU("Bergbau"),
	VERTRAGSFIRMA("Vertragsfirma"),
	WEITERVERARBEITUNG("Weiterverarbeitung"),
	MINIMARKT("Minimarkt"),
	LANDWIRTSCHAFT("Landwirtschaft"),
	WASSERVERSORGUNG("Wasserversorgung"),
	ENERGIEPRODUZENT("Energieproduzent"),
	MÜLLENTSORGUNG("Müllabfuhr"),
	BAUARBEITER("Bauarbeiter");
	
	private String translation;
	private FirmTypes(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}
	
	public void setTranslation(String translation) {
		this.translation = translation;
	}
}
