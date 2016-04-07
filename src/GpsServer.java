import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GpsServer {
	static Logger logger;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MultiThreadedServer server = new MultiThreadedServer(8888);
		new Thread(server).start();
	}

}
