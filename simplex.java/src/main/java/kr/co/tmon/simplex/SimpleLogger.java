package kr.co.tmon.simplex;

import java.io.PrintWriter;

public class SimpleLogger implements ILogger {

	private PrintWriter writer;
	
	@Override
	public void write(String log) {
		if (writer != null) {
			writer.println(log);
		}
	}
	
	public SimpleLogger(PrintWriter writer) {
		this.writer = writer; 
	}

	@Override
	public void write(Throwable throwable) {
		if (writer != null) {
			throwable.printStackTrace(writer);	
		}
	}
}
