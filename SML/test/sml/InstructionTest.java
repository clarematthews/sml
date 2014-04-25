package sml;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sml.Instruction;
import sml.Machine;

public class InstructionTest {

	private Machine m;
	private ByteArrayOutputStream outContent;
	
	@Before
	public void setUp() throws Exception {
		m = new Machine();
		m.setPc(0);
		m.setRegisters(new Registers());
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void tearDown() {
		System.setOut(null);
	}

	@Test
	public void testAdd() {
		
		Instruction LinIns0 = new LinInstruction("f0", 1, 5);
		LinIns0.execute(m);
		Instruction LinIns1 = new LinInstruction("f1", 2, -1);
		LinIns1.execute(m);
		
		assertEquals(m.getRegisters().getRegister(1), 5);
		assertEquals(m.getRegisters().getRegister(2), -1);
		
		Instruction addIns = new AddInstruction("f2", 1, 1, 2);
		addIns.execute(m);
		assertEquals(m.getRegisters().getRegister(1), 4);
		
		String expectedString = "f2: add 1 + 2 to 1";
		String actualString = addIns.toString();
		assertEquals(expectedString, actualString);
		
	}
	
	@Test
	public void testSub() {
		
		Instruction LinIns0 = new LinInstruction("f0", 4, 8);
		LinIns0.execute(m);
		Instruction LinIns1 = new LinInstruction("f1", 2, -1);
		LinIns1.execute(m);
		
		assertEquals(m.getRegisters().getRegister(4), 8);
		assertEquals(m.getRegisters().getRegister(2), -1);
		
		Instruction subIns = new SubInstruction("f3", 10, 4, 2);
		subIns.execute(m);
		assertEquals(m.getRegisters().getRegister(10), 9);
		
		String expectedString = "f3: sub 4 - 2 to 10";
		String actualString = subIns.toString();
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testBnz() {
		
		m.getLabels().addLabel("f1");
		m.getLabels().addLabel("f4");
		
		Instruction LinIns0 = new LinInstruction("f1", 1, 6);
		LinIns0.execute(m);
		
		assertEquals(m.getRegisters().getRegister(1), 6);
		
		Instruction bnzIns1 = new BnzInstruction("f2", 1, "f4");
		bnzIns1.execute(m);
		assertEquals(m.getPc(), 1);
		
		Instruction bnzIns2 = new BnzInstruction("f3", 5, "f1");
		bnzIns2.execute(m);
		assertEquals(m.getPc(), 1);
		
		String expectedString = "f3: bnz condition register 5 next label if true f1";
		String actualString = bnzIns2.toString();
		assertEquals(expectedString, actualString);
		
	}
	
	@Test
	public void testDiv() {
		Instruction LinIns0 = new LinInstruction("f1", 1, 6);
		LinIns0.execute(m);
		
		assertEquals(m.getRegisters().getRegister(1), 6);
		
		Instruction divIns1 = new DivInstruction("f2", 4, 1, 2);
		divIns1.execute(m);
		String expectedString = "Divide by zero. Register 4 not updated.\n";
		assertEquals(expectedString, outContent.toString());
		
		Instruction LinIns1 = new LinInstruction("f1", 2, 4);
		LinIns1.execute(m);
		Instruction divIns2 = new DivInstruction("f3", 4, 1, 2);
		divIns2.execute(m);
		assertEquals(m.getRegisters().getRegister(4), 1);
		
		expectedString = "f3: div 1 / 2 to 4";
		String actualString = divIns2.toString();
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testMul() {
		Instruction LinIns0 = new LinInstruction("f1", 1, 6);
		LinIns0.execute(m);
		Instruction LinIns1 = new LinInstruction("f0", 0, 3);
		LinIns1.execute(m);
		
		assertEquals(m.getRegisters().getRegister(1), 6);
		assertEquals(m.getRegisters().getRegister(0), 3);
		
		Instruction mulIns = new MulInstruction("f2", 5, 1, 0);
		mulIns.execute(m);
		assertEquals(m.getRegisters().getRegister(5), 18);
		
		String expectedString = "f2: mul 1 x 0 to 5";
		String actualString = mulIns.toString();
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testOut() {
		Instruction LinIns0 = new LinInstruction("f1", 1, 6);
		LinIns0.execute(m);
		assertEquals(m.getRegisters().getRegister(1), 6);
		
		Instruction outIns = new OutInstruction("f7", 1);
		outIns.execute(m);
		assertEquals("6\n", outContent.toString());
		
		String expectedString = "f7: out 1";
		String actualString = outIns.toString();
		assertEquals(expectedString, actualString);
	}

}
