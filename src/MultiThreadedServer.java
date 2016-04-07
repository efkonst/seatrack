import java.net.ServerSocket;

import java.net.Socket;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

public class MultiThreadedServer implements Runnable {

	protected int serverPort = 8080;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	static Logger logger;
	public volatile List<String> validIMEIs;

	public MultiThreadedServer(int port) {
		logger = Logger.getLogger("MyLog");

		this.serverPort = port;
		this.validIMEIs = new ArrayList<String>();
		logger = Logger.getLogger("MyLog");
		FileHandler fh;

		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("MyLogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("My first log");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		logger.info("Server Started");
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			logger.info("Starting Thread");
			new Thread(new Telktonika(clientSocket, "Multithreaded Server")).start();
		}
		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + this.serverPort, e);
		}
	}

}