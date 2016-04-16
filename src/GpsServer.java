import java.io.*;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GpsServer {
	static Logger logger;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MultiThreadedServer server = new MultiThreadedServer(8888);
		new Thread(server).start();
		
/*		while(true){
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		System.out.println("**** THREADS**** : "+threadArray.length);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
*/
	}

}

