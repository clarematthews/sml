/**
 * 
 */
package sml;

/**
 * This class executes a multiply instruction on registers.
 * 
 * @author cmatth04
 *
 */
public class MulInstruction extends Instruction {

	private int result;
	private int op1;
	private int op2;
	
	public MulInstruction(String label, String op) {
		super(label, op);
	}
	
	public MulInstruction(String label, int result, int op1, int op2) {
		super(label, "mul");
		this.result = result;
		this.op1 = op1;
		this.op2 = op2;
	}

	/* (non-Javadoc)
	 * @see sml.Instruction#execute(sml.Machine)
	 */
	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(result, value1*value2); 
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + op1 + " x " + op2 + " to " + result;
	}

}
