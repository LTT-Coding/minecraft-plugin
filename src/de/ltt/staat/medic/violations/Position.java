package de.ltt.staat.medic.violations;

public enum Position {
	OBERARM_RECHTS("rechter Oberarm"),
	UNTERARM_RECHTS("rechter Unterarm"),
	OBERARM_LINKS("linker Oberarm"),
	UNTERARM_LINKS("linker Unterarm"),
	HAND_RECHTS("rechte Hand"),
	HAND_LINKS("linke Hand"),
	DAUMEN_RECHTS("rechter Daumen"),
	DAUMEN_LINKS("linker Daumen"),
	ZEIGE_RECHTS("rechter Zeigefinger"),
	ZEIGE_LINKS("linker Zeigefinger"),
	MITTEL_RECHTS("rechter Mittelfinger"),
	MITTEL_LINKS("linker Mittelfinger"),
	RING_RECHTS("rechter Ringfinger"),
	RING_LINKS("linker Ringfinger"),
	KLEINER_RECHTS("rechter kleiner Finger"),
	KLEINER_LINKS("linker kleiner Finger"),
	SPEISERÖHRE("Speiseröhre"),
	LUFTROEHRE("Luftröhre"),
	LUNGE("Lunge"),
	MAGEN("Magen"),
	LEBER("Leber"),
	GALLE("Galle"),
	NIEREN("Nieren"),
	BAUCHSPEICHELDRUESE("Bauchspeicheldrüse"),
	HERZ("Herz"),
	MILZ("Milz"),
	SCHILDDRUESE("Schilddrüse"),
	BLASE("Blase"),
	DUENNDARM("Dünndarm"),
	DICKDARM("Dickdarm"), 
	GESCHLECHTSORGANE("Geschlechtsorgane"),
	BLINDDARM("Blinddarm"),
	WURMFORTSATZ("Wurmfortsatz"),
	OBERSCHENKEL_RECHTS("rechter Oberschenkel"),
	OBERSCHENKEL_LINKS("linker Oberschenkel"),
	SCHIENBEIN_RECHTS("rechtes Schienbein"),
	SCHIENBEIN_LINKS("linkes Schienbein"),
	WADE_RECHTS("rechte Wade"),
	WADE_LINKS("linke Wade"),
	FUSS_RECHTS("rechter Fuß"),
	FUSS_LINKS("Dein linker Fuß"),
	ZEHEN_RECHTS("Deine rechten Zehen"),
	ZEHEN_LINKS("Deine linken Zehen");
	
	private Position(String translation) {
		this.translation = translation;
	}

	private String translation;

	public String getTranslation() {
		return translation;
	}
}
