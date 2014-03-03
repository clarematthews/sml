package sml;

public class OutInstruction extends Instruction {
	
	private int op1;

	public OutInstruction(String l, String op) {
		super(l, op);
	}
	
	public OutInstruction(String label, int op1) {
		this(label, "out");
		this.op1 = op1;
	}
	
	@Override
	public void execute(Machine m) {
		int value = m.getRegisters().getRegister(op1);
		System.out.println(value);
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + op1;
	}

}
