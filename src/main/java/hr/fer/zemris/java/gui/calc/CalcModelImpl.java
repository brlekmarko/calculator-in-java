package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class CalcModelImpl implements CalcModel, CalcValueListener{
	
	private boolean isEditable;
	private boolean isPositive;
	private String currentInput;
	private double currentInputDecimal;
	private String frozenInput;
	
	public Double activeOperand;
	public DoubleBinaryOperator pendingOperation;
	
	private List<CalcValueListener> listeners = new ArrayList<>();
	
	
	
	public CalcModelImpl() {
		super();
		isEditable = true;
		isPositive = true;
		currentInput = "";
		currentInputDecimal = 0;
		frozenInput = null;
		
		activeOperand = null;
		pendingOperation = null;
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if(l == null) {
			throw new NullPointerException();
		}
		listeners.add(l);
		
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		if(l == null) {
			throw new NullPointerException();
		}
		listeners.remove(l);
		
	}

	@Override
	public double getValue() {
		return this.isPositive ? this.currentInputDecimal : -1 * this.currentInputDecimal;
	}

	@Override
	public void setValue(double value) {
		
		if(value < 0 ) {
			value*=-1;
			this.isPositive = false;
		}
		else {
			this.isPositive = true;
		}
		this.currentInputDecimal = value;
		this.currentInput = value + "";
		this.frozenInput = value + "";
		this.valueChanged(this);
		this.isEditable = false;
		
	}

	@Override
	public boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public void clear() {
		this.currentInput = "";
		this.currentInputDecimal = 0;
		this.frozenInput = null;
		this.isEditable = true;
		this.isPositive = true;
		this.valueChanged(this);
		
	}

	@Override
	public void clearAll() {
		this.clear();
		this.activeOperand = null;
		this.pendingOperation = null;
		
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if(!this.isEditable) throw new CalculatorInputException();
		this.isPositive = !this.isPositive;
		this.valueChanged(this);
		
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!this.isEditable) throw new CalculatorInputException();
		if(this.currentInput.contains(".") || this.currentInput.equals("")) {
			throw new CalculatorInputException();
		}
		this.currentInput += ".";
		this.frozenInput += ".";
		this.valueChanged(this);
		
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!isEditable) {
			throw new CalculatorInputException();
		}
		if(digit<0 || digit>9) {
			throw new IllegalArgumentException();
		}
		
		String tryNew = this.currentInput + digit;
		if(this.currentInput.equals("0")) {
			tryNew = digit + "";
		}
		try {
			double tryNewDecimal = Double.parseDouble(tryNew);
			this.currentInput = tryNew;
			this.frozenInput = tryNew;
			this.currentInputDecimal = tryNewDecimal;
			this.valueChanged(this);
		}catch(Exception e) {
			throw new CalculatorInputException();
		}
		
	}

	@Override
	public boolean isActiveOperandSet() {
		return this.activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(this.activeOperand == null) {
			throw new IllegalStateException();
		}
		return this.activeOperand.doubleValue();
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		
	}

	@Override
	public void clearActiveOperand() {
		this.activeOperand = null;
		
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return this.pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;
		
	}
	
	public boolean isFrozenNull() {
		return this.frozenInput == null;
	}
	
	public String toString() {
		if(this.frozenInput != null) {
			return this.isPositive ? this.frozenInput : "-"+this.frozenInput;
		}
		else {
			return this.isPositive ? "0" : "-0";
		}
	}

	@Override
	public void valueChanged(CalcModel model) {
		for(CalcValueListener listener: listeners) {
			listener.valueChanged(model);
		}
		
	}

}
