package kr.co.tmon.simplex;

public interface ILogger {
	
	void write(String log);
	
	void write(Throwable throwable);
}
