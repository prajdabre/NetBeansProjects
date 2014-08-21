package in.ac.iitb.cfilt.common.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DummyConsole.java
 *
 * Created On: 30-Jan-08
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */

/**
 * <p><b>Class</b>	: DummyConsole
 * <p><b>Purpose</b>	: This class is 
 */
public class DummyConsole extends PrintStream {

	/**
	 * <b>Constructor</b>
	 * @param out
	 */
	public DummyConsole(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public DummyConsole(String fileName) throws FileNotFoundException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param file
	 * @throws FileNotFoundException
	 */
	public DummyConsole(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param out
	 * @param autoFlush
	 */
	public DummyConsole(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param fileName
	 * @param csn
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public DummyConsole(String fileName, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param file
	 * @param csn
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public DummyConsole(File file, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(file, csn);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Constructor</b>
	 * @param out
	 * @param autoFlush
	 * @param encoding
	 * @throws UnsupportedEncodingException
	 */
	public DummyConsole(OutputStream out, boolean autoFlush, String encoding)
			throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void println(String x) {
		//Do nothing
		
	}

	@Override
	public void println() {
		// Do nothing
	}
	
	@Override
	public void println(Object x) {
		// Do nothing
	}
}
