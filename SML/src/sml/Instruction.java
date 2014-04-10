package sml;

/**
 * This class is the superclass of the classes for machine instructions
 *
 */
public abstract class Instruction {
	protected String label;
	protected String opcode;

	/**
	 * Constructor: an instruction with label l and opcode op
	 * (op must be an operation of the language)
	 * @param l  the instruction label
	 * @param op the instruction opcode
	 */
	public Instruction(String l, String op) {
		this.label = l;
		this.opcode = op;
	}

	/**
	 * @return the representation "label: opcode" of this Instruction
	 */ 
	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	/**
	 * Execute this instruction.
	 * @param m the machine to execute instruction on.
	 */ 
	public abstract void execute(Machine m);
}
