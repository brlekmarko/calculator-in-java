package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

public class CalcLayout implements LayoutManager2{
	
	public static int BROJ_REDAKA = 5;
	public static int BROJ_STUPACA = 7;
	
	
	private int razmak;
	private Component[][] komponente;
	
	/**
	 * Specifican konstruktor, prima razmak izmedju redaka/stupaca
	 * @param razmak
	 */
	public CalcLayout(int razmak) {
		this.razmak = razmak;
		this.komponente = new Component[BROJ_REDAKA][BROJ_STUPACA];
		
		Component[] novaLista;
		
		for (int i=0;i<BROJ_REDAKA;i++) {
			novaLista = new Component[BROJ_STUPACA];
			for (int j=0; j<BROJ_STUPACA;j++) {
				novaLista[j] = null;
			}
			komponente[i] = novaLista;
		}
	}
	
	/**
	 * Prazan konstruktor, delegira se na specifican
	 */
	public CalcLayout() {
		new CalcLayout(0);
	}
	
	

	/**
	 * U zadatku receno da se baca exception
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * Brise komponentu iz liste
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		
		for (int i=0;i<BROJ_REDAKA;i++) {
			for (int j=0; j<BROJ_STUPACA;j++) {
				if(komponente[i][j] == comp) {
					komponente[i][j] = null;
					return;
				}
			}
		}
		throw new IllegalArgumentException();
		
	}

	
	/**
	 * Vraca preferirane dimenzije, racuna se kao najveca preferirana dimenzija
	 * komponente * broj stupaca/redaka + razmaci
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension pref = this.getMinimumOrMaximum("PREF");
		if(pref == null) {
			return new Dimension(0,0);
		}
		Insets border = parent.getInsets();
		
		int newWidth = pref.width*BROJ_STUPACA + razmak * (BROJ_STUPACA-1);
		newWidth+=border.left + border.right;
		
		int newHeight = pref.height*BROJ_REDAKA + razmak * (BROJ_REDAKA-1);
		newHeight+=border.top + border.bottom;
		
		return new Dimension(newWidth, newHeight);
	}

	/**
	 * Vraca minimalne dimenzije, racuna se kao najveca minimalna dimenzija
	 * komponente * broj stupaca/redaka + razmaci
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension min = this.getMinimumOrMaximum("MIN");
		if(min == null) {
			return new Dimension(0,0);
		}
		Insets border = parent.getInsets();
		
		int newWidth = min.width*BROJ_STUPACA + razmak * (BROJ_STUPACA-1);
		newWidth+=border.left + border.right;
		
		int newHeight = min.height*BROJ_REDAKA + razmak * (BROJ_REDAKA-1);
		newHeight+=border.top + border.bottom;
		
		return new Dimension(newWidth, newHeight);
	}
	
	/**
	 * Vraca maksimalne dimenzije, racuna se kao najmanja maksimalna dimenzija
	 * komponente * broj stupaca/redaka + razmaci
	 */
	@Override
	public Dimension maximumLayoutSize(Container parent) {
		Dimension max = this.getMinimumOrMaximum("MAX");
		if(max == null) {
			return new Dimension(0,0);
		}
		Insets border = parent.getInsets();
		
		int newWidth = max.width*BROJ_STUPACA + razmak * (BROJ_STUPACA-1);
		newWidth+=border.left + border.right;
		
		int newHeight = max.height*BROJ_REDAKA + razmak * (BROJ_REDAKA-1);
		newHeight+=border.top + border.bottom;
		
		return new Dimension(newWidth, newHeight);
	}

	/**
	 * Razmjesta elemente po ekranu
	 */
	@Override
	public void layoutContainer(Container parent) {
		int width = parent.getWidth();
		int height = parent.getHeight();
		Insets insets = parent.getInsets();
		
		int leftoverWidth = width - insets.left - insets.right - razmak*(BROJ_STUPACA-1);
		int leftoverHeight = height - insets.top - insets.bottom - razmak*(BROJ_REDAKA-1);
		
		double columnWidth = leftoverWidth / BROJ_STUPACA;
		double rowHeight = leftoverHeight / BROJ_REDAKA;
		
		int preostaloSirine = width - (int)columnWidth * BROJ_STUPACA;
		boolean naizmjenicniStupac = true;
		
		int preostaloVisine = height - (int)rowHeight * BROJ_REDAKA;
		boolean naizmjenicniRedak = true;
		
		int brojacRedaka = 0;
		int boundsHeight;
		int x,y;
		for(Component[] redak : komponente) {
			boundsHeight=(int)rowHeight;
			if(naizmjenicniStupac && preostaloVisine>0) {
				boundsHeight+=1;
				preostaloVisine-=1;
				naizmjenicniStupac = false;
			}else {
				naizmjenicniStupac = true;
			}
			
			int brojacStupaca = 0;
			int boundsWidth;
			for(Component comp : redak) {
				if(comp == null) {
					brojacStupaca++;
					continue;
				}
				x = insets.left + brojacStupaca * razmak + brojacStupaca * (int)columnWidth;
				y = insets.top + brojacRedaka * razmak + brojacRedaka * (int)rowHeight;
				
				boundsWidth = (int)columnWidth;
				if(naizmjenicniRedak && preostaloSirine>0) {
					boundsWidth+=1;
					preostaloSirine-=1;
					naizmjenicniRedak = false;
				}else {
					naizmjenicniRedak = true;
				}
				
				if(brojacRedaka == 0 && brojacStupaca == 0) {
					comp.setBounds(x, y, preostaloSirine>=3 ? (int)columnWidth*5 + 3 + razmak*3 : (int)columnWidth*5 + preostaloSirine + razmak*4, boundsHeight);
				}
				else {
					comp.setBounds(x, y, boundsWidth, boundsHeight);
				}
				brojacStupaca++;
			}
			brojacRedaka++;
		}
		
		
	}

	
	/**
	 * Constraints mora biti tipa RCPosition ili string sa valjanim koordinatama.
	 * 
	 * @throws CalcLayoutException ako vec postoji komponenta s istim ogranicenjem, ili je ogranicenje krivo
	 * @throws NullPointerException ako je neki od parametara null
	 * @throws IllegalArgumentException ako constraints nije niti string niti objekt tipa RCPosition
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(comp == null || constraints == null) {
			throw new NullPointerException();
		}
		
		RCPosition param;
		
		if(constraints instanceof RCPosition) {
			param = (RCPosition)constraints;
		}
		else if(constraints instanceof String) {
			param = RCPosition.parse((String)constraints);
		}
		else {
			throw new IllegalArgumentException();
		}
		
		if(komponente[param.getRedak()-1][param.getStupac()-1] != null) {
			throw new CalcLayoutException("Pozicija se vec koristi");
		}
		
		komponente[param.getRedak()-1][param.getStupac()-1] = comp;
		
		
		
	}

	
	/**
	 * Možemo vratiti 0
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}
	
	
	/**
	 * Možemo vratiti 0
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// valjda prazno ?
		
	}
	
	
	/**
	 * Pomocna funkcija za racunanje minimalnih, preferiranih i maksimalnih dimenzija
	 * 
	 * @param minOrMax koju dimenziju trazimo
	 * @return minimalna, preferirana ili maksimalna dimenzija
	 */
	public Dimension getMinimumOrMaximum(String minOrMax) {
		int minWidth = -1;
		int minHeight = -1;
		
		int maxWidth = -1;
		int maxHeight = -1;
		
		int prefWidth = -1;
		int prefHeight = -1;
		
		int currentMaxHeight;
		int currentMaxWidth;
		
		int currentMinHeight;
		int currentMinWidth;
		
		int currentPrefHeight;
		int currentPrefWidth;
		
		//trazimo najvecu minimalnu
		//trazimo najmanju maksimalnu
		//trazimo najvecu preferiranu
		
		//treba pripazit na sirinu od (1,1)
		
		Component current;
		
		for (int i=0;i<BROJ_REDAKA;i++) {
			for (int j=0; j<BROJ_STUPACA;j++) {
				current = komponente[i][j];
				if(current == null) continue;
				currentMaxWidth = current.getMaximumSize().width;
				currentMaxHeight = current.getMaximumSize().height;
				currentMinWidth = current.getMinimumSize().width;
				currentMinHeight = current.getMinimumSize().height;
				currentPrefWidth = current.getPreferredSize().width;
				currentPrefHeight = current.getPreferredSize().height;
				
				if(i==0 && j==0) {
					currentMaxWidth-=razmak*4;
					currentMinWidth-=razmak*4;
					currentPrefWidth-=razmak*4;
					
					currentMaxWidth= (int)(currentMaxWidth/5);
					currentMinWidth= (int)(currentMinWidth/5);
					currentPrefWidth= (int)(currentPrefWidth/5);
				}
				
				if((currentMaxWidth>0 && currentMaxWidth < maxWidth) || maxWidth == -1) {
					maxWidth = currentMaxWidth;
				}
				if((currentMaxHeight>0 && currentMaxHeight < maxHeight) || maxHeight == -1) {
					maxHeight = currentMaxHeight;
				}
				if(currentMinWidth > minWidth || minWidth == -1) {
					minWidth = currentMinWidth;
				}
				if(currentMinHeight > minHeight || minHeight == -1) {
					minHeight = currentMinHeight;
				}
				if(currentPrefWidth > prefWidth || prefWidth == -1) {
					prefWidth = currentPrefWidth;
				}
				if(currentPrefHeight > prefHeight || prefHeight == -1) {
					prefHeight = currentPrefHeight;
				}
			}
		}
		
		if(minOrMax.equals("MAX")) {
			if(maxWidth == -1) {
				return null;
			}
			else {
				return new Dimension(maxWidth, maxHeight);
			}
		}
		else if(minOrMax.equals("MIN")) {
			if(minWidth == -1) {
				return null;
			}
			else {
				return new Dimension(minWidth, minHeight);
			}
		}
		else if(minOrMax.equals("PREF")) {
			if(prefWidth == -1) {
				return null;
			}
			else {
				return new Dimension(prefWidth, prefHeight);
			}
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	

}
