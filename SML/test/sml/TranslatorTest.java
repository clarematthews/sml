package sml;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {

	private ArrayList<Instruction> prog;
	private Labels labels;
	private ByteArrayOutputStream errContent;
	private ByteArrayOutputStream outContent;
	
	@Before
	public void setUp() throws Exception {
		prog = new ArrayList<>();
		labels = new Labels();
		errContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errContent));
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void tearDown() {
		System.setErr(null);
		System.setOut(null);
	}

	@Test
	public void testNoFile() {
		Translator t = new Translator("nofile.txt");
		t.readAndTranslate(labels, prog);
		assertEquals("File: IO error to start src/nofile.txt (No such file or directory)\n", outContent.toString());
	}
	
	@Test
	public void testInvalidOp() {
		Translator t = new Translator("fixtures/test1.txt");
		t.readAndTranslate(labels, prog);
		assertEquals("Error. Instruction class not found.\n", errContent.toString());
	}
	
	@Test
	public void testIn() {
		Translator t = new Translator("fixtures/test2.txt");
		t.readAndTranslate(labels, prog);
		assertEquals(0, labels.indexOf("f0"));
		
		assertThat(prog.get(0), instanceOf(LinInstruction.class));
	}
	
	@Test
	public void testReadFile() {
		Translator t = new Translator("fixtures/test3.txt");
		t.readAndTranslate(labels, prog);
		
		assertEquals(0, labels.indexOf("f1"));
		assertEquals(1, labels.indexOf("f2"));
		assertEquals(2, labels.indexOf("f3"));
		assertEquals(3, labels.indexOf("f4"));
		assertEquals(4, labels.indexOf("f5"));
		assertEquals(5, labels.indexOf("f6"));
		
		assertThat(prog.get(0), instanceOf(AddInstruction.class));
		assertThat(prog.get(1), instanceOf(BnzInstruction.class));
		assertThat(prog.get(2), instanceOf(MulInstruction.class));
		assertThat(prog.get(3), instanceOf(DivInstruction.class));
		assertThat(prog.get(4), instanceOf(OutInstruction.class));
		assertThat(prog.get(5), instanceOf(SubInstruction.class));
	}

}
