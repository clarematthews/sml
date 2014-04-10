package sml;

/**
 * This class executes a boolean non-zero instruction on registers.
 *
 */
public class BnzInstruction extends Instruction {

	private int op1;
	private String l2;
	
	
	public BnzInstruction(String label, String op) {
		super(label, op);
	}
	
	public BnzInstruction(String label, int op1, String l2) {
		this(label, "bnz");
		this.op1 = op1;
		this.l2 = l2;
	}

	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		if(value1 != 0) 
			m.setPc(m.getLabels().indexOf(l2));
	}
	
	@Override
	public String toString() {
		return super.toString() + " condition register " + op1 + " next label if true " + l2;
	}

}
