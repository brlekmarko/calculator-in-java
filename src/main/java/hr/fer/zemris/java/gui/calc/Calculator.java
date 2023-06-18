package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

public class Calculator extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CalcModelImpl calcImpl;
	private List<Double> stog;
	boolean isInverted = false;
	
	
	//ovim tipkama moramo mjenjati text na klik na checkbox
	private JButton changableLog = unaryOperationButton("log");
	private JButton changableSin = unaryOperationButton("sin");
	private JButton changableCos = unaryOperationButton("cos");
	private JButton changableLn = unaryOperationButton("ln");
	private JButton changableTan = unaryOperationButton("tan");
	private JButton changableXToPowerN = binaryOperationButton("x^n");
	private JButton changableCtg = unaryOperationButton("ctg");
	
	
	public Calculator() {
		this.calcImpl = new CalcModelImpl();
		this.stog = new ArrayList<>();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}
	
	
	private void initGUI() {
		Container cp = getContentPane();
		

		
		cp.setLayout(new CalcLayout(3));
		
		cp.add(ekran(), "1,1");
		cp.add(binaryOperationButton("="), "1,6");
		cp.add(clearButton(), "1,7");
		
		cp.add(unaryOperationButton("1/x"), "2,1");
		cp.add(changableSin, "2,2");
		cp.add(digitButton(7), "2,3");
		cp.add(digitButton(8), "2,4");
		cp.add(digitButton(9), "2,5");
		cp.add(binaryOperationButton("/"), "2,6");
		cp.add(resetButton(), "2,7");
		
		cp.add(changableLog, "3,1");
		cp.add(changableCos, "3,2");
		cp.add(digitButton(4), "3,3");
		cp.add(digitButton(5), "3,4");
		cp.add(digitButton(6), "3,5");
		cp.add(binaryOperationButton("*"), "3,6");
		cp.add(pushButton(), "3,7");
		
		cp.add(changableLn, "4,1");
		cp.add(changableTan, "4,2");
		cp.add(digitButton(1), "4,3");
		cp.add(digitButton(2), "4,4");
		cp.add(digitButton(3), "4,5");
		cp.add(binaryOperationButton("-"), "4,6");
		cp.add(popButton(), "4,7");
		
		cp.add(changableXToPowerN, "5,1");
		cp.add(changableCtg, "5,2");
		cp.add(digitButton(0), "5,3");
		cp.add(swapSignsButton(), "5,4");
		cp.add(decimalButton(), "5,5");
		cp.add(binaryOperationButton("+"), "5,6");
		cp.add(inverter(),"5,7");
		
	}
	
	
	/**
	 * Predstavlja tipku sa znamenkom.
	 * 
	 * @param digit znamenka
	 * @return tipka sa znamenkom
	 */
	private JButton digitButton(int digit) {
		if(digit<0 || digit>9) {
			throw new IllegalArgumentException();
		}
		JButton btn = new JButton("" + digit);
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(30f));

		btn.addActionListener(e -> {
			calcImpl.insertDigit(digit);
		});
		return btn;
	}
	
	/**
	 * Predstavlja tipku decimalne točke
	 * 
	 * @return tipka decimalne točke
	 */
	private JButton decimalButton() {
		JButton btn = new JButton(".");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(30f));

		btn.addActionListener(e -> {
			calcImpl.insertDecimalPoint();
		});
		return btn;
		
	}
	
	/**
	 * Predstavlja tipku binarne operacije (+,-,*,/..)
	 * 
	 * @param op Binarna operacija
	 * @return tipka binarne operacije
	 */
	private JButton binaryOperationButton(String op) {
		JButton btn = new JButton(op);
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(30f));
		
		if(op.equals("x^n")) {
			btn.setFont(btn.getFont().deriveFont(20f));
		}

		btn.addActionListener(e -> {
			if(!calcImpl.isFrozenNull()) {
				if(calcImpl.isActiveOperandSet()) {
					double result = calcImpl.getPendingBinaryOperation().applyAsDouble(calcImpl.getActiveOperand(), calcImpl.getValue());
					calcImpl.clearAll();
					if(op.equals("=")) {
						calcImpl.setValue(result);
					}
					else{
						calcImpl.setValue(result);
						calcImpl.setActiveOperand(result);
						calcImpl.clear();
						if(op.equals("x^n")) {
							if(!this.isInverted) {
								calcImpl.setPendingBinaryOperation((prvi, drugi) -> Math.pow(prvi, drugi));
							}
							else {
								calcImpl.setPendingBinaryOperation((prvi, drugi) -> Math.pow(prvi, 1/drugi));
							}
						}
						else {
							calcImpl.setPendingBinaryOperation(getOperation(op));
						}
					}
				}
				else {
					calcImpl.setActiveOperand(calcImpl.getValue());
					calcImpl.clear();
					if(op.equals("x^n")) {
						if(!this.isInverted) {
							calcImpl.setPendingBinaryOperation((prvi, drugi) -> Math.pow(prvi, drugi));
						}
						else {
							calcImpl.setPendingBinaryOperation((prvi, drugi) -> Math.pow(prvi, 1/drugi));
						}
					}
					else {
						calcImpl.setPendingBinaryOperation(getOperation(op));
					}
				}
			}else {
				throw new CalculatorInputException();
			}
		});
		return btn;
	}
	
	/**
	 * Predstavlja ekran kalkulatora, ispisuje value of calcImpl-a
	 * 
	 * @return ekran kalkulatora
	 */
	private JLabel ekran() {
		JLabel ekran = new JLabel("0");
		ekran.setBackground(Color.YELLOW);
		ekran.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		ekran.setOpaque(true);
		ekran.setFont(ekran.getFont().deriveFont(30f));
		ekran.setHorizontalAlignment(SwingConstants.RIGHT);
		
		calcImpl.addCalcValueListener((calcImpl) -> {
			ekran.setText(calcImpl.toString());
		});
		
		
		return ekran;
	}
	
	/**
	 * Predstavlja tipku clear, čisti vrijednost s ekrana.
	 * 
	 * @return tipka clear
	 */
	private JButton clearButton() {
		JButton btn = new JButton("clr");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(20f));

		btn.addActionListener(e -> {
			calcImpl.clear();
		});
		return btn;
	}
	
	/**
	 * Predstavlja tipku reset, vrača kalkulator u početno stanje.
	 * 
	 * @return tipka reset
	 */
	private JButton resetButton() {
		JButton btn = new JButton("reset");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(20f));

		btn.addActionListener(e -> {
			calcImpl.clearAll();
		});
		return btn;
	}
	
	/**
	 * Predstavlja tipku promjene predznaka.
	 * 
	 * @return tipka promjene predznaka
	 */
	private JButton swapSignsButton() {
		JButton btn = new JButton("+/-");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(30f));

		btn.addActionListener(e -> {
			calcImpl.swapSign();
		});
		return btn;
	}
	
	/**
	 * Predstavlja tipku push, stavlja broj sa ekrana na vrh stoga.
	 * 
	 * @return tipa push
	 */
	private JButton pushButton() {
		JButton btn = new JButton("push");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(20f));

		btn.addActionListener(e -> {
			stog.add(calcImpl.getValue());
		});
		return btn;
	}
	
	/**
	 * Predstavlja tipku pop, stavlja broj sa vrha stoga na ekran.
	 * 
	 * @return tipa pop
	 */
	private JButton popButton() {
		JButton btn = new JButton("pop");
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(20f));

		btn.addActionListener(e -> {
			if(stog.size()>0) {
				calcImpl.setValue(stog.get(stog.size()-1));
				stog.remove(stog.size()-1);
			}
			else {
				JDialog d = new JDialog(this, "Stack is empty.", true);
				JLabel l = new JLabel("Nothing to pop.");
				d.add(l);
				
				d.setSize(100, 100);
				d.setLocation(this.getX() + (this.getWidth() - d.getWidth())/2, this.getY() + (this.getHeight() - d.getHeight())/2);
				
				d.setVisible(true);
			}
		});
		return btn;
	}
	
	
	/**
	 * Predstavlja checkbox koji invertira neke operacije.
	 * 
	 * @return checkbox
	 */
	private JCheckBox inverter() {
		JCheckBox checkbox = new JCheckBox("Inv");   
		checkbox.setFont(checkbox.getFont().deriveFont(20f));
		checkbox.addItemListener((e) -> {
			this.isInverted = e.getStateChange()==1 ? true : false;
			changeButtons();
		});
		return checkbox;
	}
	
	/**
	 * Predstavlja tipke unarne operacije, npr. sin, cos..
	 * 
	 * @param text unarna operacija
	 * @return tipka unarne operacije
	 */
	private JButton unaryOperationButton(String text) {
		JButton btn = new JButton(text);
		btn.setBackground(Color.MAGENTA);
		btn.setFont(btn.getFont().deriveFont(20f));

		btn.addActionListener(e -> {
			
			if(text.equals("1/x")) {
				calcImpl.setValue(1/calcImpl.getValue());
				return;
			}
			if(text.equals("sin")) {
				if(!this.isInverted) {
					calcImpl.setValue(Math.sin(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.asin(calcImpl.getValue()));
				}
			}
			if(text.equals("cos")) {
				if(!this.isInverted) {
					calcImpl.setValue(Math.cos(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.acos(calcImpl.getValue()));
				}
			}
			if(text.equals("tan")) {
				if(!this.isInverted) {
					calcImpl.setValue(Math.tan(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.atan(calcImpl.getValue()));
				}
			}
			if(text.equals("ctg")) {
				if(!this.isInverted) {
					calcImpl.setValue(1/Math.tan(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.atan(1/calcImpl.getValue()));
				}
			}
			if(text.equals("log")) {
				if(!this.isInverted) {
					calcImpl.setValue(Math.log10(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.pow(10,calcImpl.getValue()));
				}
			}
			if(text.equals("ln")) {
				if(!this.isInverted) {
					calcImpl.setValue(Math.log(calcImpl.getValue()));
				}else {
					calcImpl.setValue(Math.pow(Math.E,calcImpl.getValue()));
				}
			}
		});
		return btn;
	}
	
	/**
	 * Na osnovi teksta vraća binarnu operaciju.
	 * 
	 * @param op string reprezentacija binarne operacije
	 * @return binarna operacija
	 */
	public DoubleBinaryOperator getOperation(String op) {
		
		if(op.equals("+")) return (double first, double second) -> first+second;
		if(op.equals("-")) return (double first, double second) -> first-second;
		if(op.equals("*")) return (double first, double second) -> first*second;
		if(op.equals("/")) return (double first, double second) -> first/second;
		throw new IllegalArgumentException();
	}	
	
	/**
	 * Prebacuje tekst tipki nakon invertiranja
	 */
	public void changeButtons() {
		if(this.isInverted) {
			changableLog.setText("10^x");
			changableSin.setText("arcsin");
			changableCos.setText("arccos");
			changableLn.setText("e^x");
			changableTan.setText("arctan");
			changableXToPowerN.setText("x^(1/n)");
			changableCtg.setText("arcctg");
		}else {
			changableLog.setText("log");
			changableSin.setText("sin");
			changableCos.setText("cos");
			changableLn.setText("ln");
			changableTan.setText("tan");
			changableXToPowerN.setText("x^n");
			changableCtg.setText("ctg");
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Calculator().setVisible(true);
		});
	}
}
