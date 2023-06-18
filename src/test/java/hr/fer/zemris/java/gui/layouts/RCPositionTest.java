package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

public class RCPositionTest {
	
	@Test
	void outOfBoundsTest1() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(-1,5);
		});
	}
	
	@Test
	void outOfBoundsTest2() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(6,5);
		});
	}
	
	@Test
	void outOfBoundsTest3() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(2,-1);
		});
	}
	
	@Test
	void outOfBoundsTest4() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(2,8);
		});
	}
	
	
	@Test
	void insideFirstElementTest1() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(1,2);
		});
	}
	
	@Test
	void insideFirstElementTest2() {
		
		assertThrows(CalcLayoutException.class, () -> {
			new RCPosition(1,5);
		});
	}
	
	
	@Test
	void samePositionTest() {
		
		assertThrows(CalcLayoutException.class, () -> {
			JPanel p = new JPanel(new CalcLayout(3));
			p.add(new JLabel("x"), "1,1");
			p.add(new JLabel("y"), "2,3");
			p.add(new JLabel("z"), new RCPosition(2,3));
		});
	}
}
