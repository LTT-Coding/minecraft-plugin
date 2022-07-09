package de.ltt.server.reflaction;

public enum Roots {
	//Player
	MONEY(".money"),
	MONEYINHAND(".moneyInHand"),
	JOB(".job"),
	RANG(".rang"),
	//Server
	FIRMS("Server.firms"),
	ADMINS("Server.Admins"),
	SUPPORTER("Server.Supporter"),
	MODERATOREN("Server.Moderatoren"),
	//Staat
	FIRMPRICE("Staat.firmprice");
	
	private String translation;
	private Roots(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}
}
