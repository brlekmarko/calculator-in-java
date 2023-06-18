package hr.fer.zemris.java.gui.layouts;

/**
 * Pozicija na CalcLayoutu, ima 5 redaka i 7 stupaca.
 * 
 * @author Marko Brlek
 *
 */
public class RCPosition {
	
	private int redak, stupac;
	
	/**
	 * Kreira objekt tipa RCPosition, provjerava valjanost podataka
	 * 
	 * @param redak
	 * @param stupac
	 * @throws CalcLayoutException ako stupac ili redak idu izvan granica
	 */
	public RCPosition(int redak, int stupac) {
		if(redak<1 || redak>5 || stupac<1 || stupac>7) {
			throw new CalcLayoutException();
		}
		if(redak == 1) {
			if(stupac == 2 || stupac == 3 || stupac == 4 || stupac == 5) {
				throw new CalcLayoutException();
			}
		}
		this.redak = redak;
		this.stupac = stupac;
	}
	
	public int getRedak() {
		return redak;
	}
	
	public int getStupac() {
		return stupac;
	}
	
	/**
	 * Pretvara string u objekt tipa RCPosition
	 * @param text string koji zelimo pretvoriti u RCPosition
	 * @throws IllegalArgumentException ako text nije valjanog oblika
	 * @return Objekt tipa RCPosition
	 */
	public static RCPosition parse(String text) {
		String[] splitted = text.split(",");
		try {
			int redak = Integer.parseInt(splitted[0]);
			int stupac = Integer.parseInt(splitted[1]);
			return new RCPosition(redak, stupac);
		}
		catch(Exception e){
			throw new IllegalArgumentException();
		}
	}
}
