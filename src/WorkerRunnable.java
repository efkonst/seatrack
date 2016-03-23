import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.net.*;

/**

 */
public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;

	public WorkerRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			long time = System.currentTimeMillis();

			// System.out.println("GOT DATA");
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String got_data_from_gps = br.readLine();
			String[] got_1 = got_data_from_gps.split(",");

			// System.out.println(got_1[1]+":"+got_1[3]);
			output.write(("*HQ," + got_1[1] + ",D1," + got_1[3] + ",30,1#\r\n").getBytes());
			while (true) {
				got_data_from_gps = br.readLine();
				if (!got_data_from_gps.contains("XT")) {
					HttpClient htclient = new HttpClient();

					
					try {
						htclient.sendGet(got_data_from_gps.substring(0, got_data_from_gps.length() - 1)+"%23");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				System.out.println(got_data_from_gps);
				output.write(("\r\n").getBytes());
			}
			// output.close();
			// input.close();

			// input.close();
			// System.out.println("Request processed: " + time);
		} catch (IOException e) {
			// report exception somewhere.
			// e.printStackTrace();
		}
	}
}
