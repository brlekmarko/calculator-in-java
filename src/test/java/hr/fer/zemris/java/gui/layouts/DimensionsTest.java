package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class DimensionsTest {

	@Test
	void testDimensions1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); 
		l1.setPreferredSize(new Dimension(10,30));
		l1.setMaximumSize(new Dimension(20,50));
		l1.setMinimumSize(new Dimension(5,10));
		JLabel l2 = new JLabel(""); 
		l2.setPreferredSize(new Dimension(20,15));
		l2.setMaximumSize(new Dimension(10,55));
		l2.setMinimumSize(new Dimension(10,10));;
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dimPref = p.getPreferredSize();
		Dimension dimMin = p.getMinimumSize();
		Dimension dimMax = p.getMaximumSize();
		
		assertEquals(152, dimPref.width);
		assertEquals(158, dimPref.height);
		assertEquals(82 , dimMax.width);
		assertEquals(258, dimMax.height);
		assertEquals(82, dimMin.width);
		assertEquals(58, dimMin.height);
		
	}
	
	@Test
	void testDimensions2(){
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);

	}
}
