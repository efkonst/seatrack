package tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

class TeltonikaClientRunnable implements Runnable {
	private Thread t;
	private String threadName;
	static int seq = 0;

	byte[] imeipacket = { 0x00, 0x0F, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31,
			0x30, 0x30 };
	byte[] message = { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0xA8, (byte) 0x08,
			(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x13, (byte) 0xfc, (byte) 0x20, (byte) 0x8d,
			(byte) 0xff, (byte) 0x00, (byte) 0x0f, (byte) 0x14, (byte) 0xf6, (byte) 0x50, (byte) 0x20, (byte) 0x9c,
			(byte) 0xca, (byte) 0x80, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0xd6, (byte) 0x04, (byte) 0x00,
			(byte) 0x04, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x01, (byte) 0x01, (byte) 0x15, (byte) 0x03,
			(byte) 0x16, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x46, (byte) 0x00, (byte) 0x00, (byte) 0x01,
			(byte) 0x5d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x13, (byte) 0xfc, (byte) 0x17,
			(byte) 0x61, (byte) 0x0b, (byte) 0x00, (byte) 0x0f, (byte) 0x14, (byte) 0xff, (byte) 0xe0, (byte) 0x20,
			(byte) 0x9c, (byte) 0xc5, (byte) 0x80, (byte) 0x00, (byte) 0x6e, (byte) 0x00, (byte) 0xc0, (byte) 0x05,
			(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x01, (byte) 0x01, (byte) 0x15,
			(byte) 0x03, (byte) 0x16, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x46, (byte) 0x00, (byte) 0x00,
			(byte) 0x01, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x13, (byte) 0xfc,
			(byte) 0x28, (byte) 0x49, (byte) 0x45, (byte) 0x00, (byte) 0x0f, (byte) 0x15, (byte) 0x0f, (byte) 0x00,
			(byte) 0x20, (byte) 0x9c, (byte) 0xd2, (byte) 0x00, (byte) 0x00, (byte) 0x95, (byte) 0x01, (byte) 0x08,
			(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x01, (byte) 0x01,
			(byte) 0x15, (byte) 0x00, (byte) 0x16, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x46, (byte) 0x00,
			(byte) 0x00, (byte) 0x01, (byte) 0x5d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x13,
			(byte) 0xfc, (byte) 0x26, (byte) 0x7c, (byte) 0x5b, (byte) 0x00, (byte) 0x0f, (byte) 0x15, (byte) 0x0a,
			(byte) 0x50, (byte) 0x20, (byte) 0x9c, (byte) 0xcc, (byte) 0xc0, (byte) 0x00, (byte) 0x93, (byte) 0x00,
			(byte) 0x68, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x01,
			(byte) 0x01, (byte) 0x15, (byte) 0x00, (byte) 0x16, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x46,
			(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x5b, (byte) 0x00, (byte) 0x04, (byte) 0x1F, (byte) 0x2c };

	TeltonikaClientRunnable(String name) {
		threadName = name;
		// System.out.println("Creating " + threadName );
	}

	public void run() {
		System.out.println("Running " + threadName + " " + message.length);
		try {
			try {
				String sentence;
				Socket clientSocket = new Socket("ekonhome.ddns.net", 8888);
				// DataOutputStream outToServer = new
				// DataOutputStream(clientSocket.getOutputStream());
				// BufferedReader inFromServer = new BufferedReader(new
				// InputStreamReader(clientSocket.getInputStream()));
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

				seq++;
				// semd toserver
				output.write(imeipacket);
				// read from server
				// sentence = inFromServer.readLine();
				// System.out.println(sentence);
				byte[] newdata = readBytes(input);
				for (int i = 0; i < 1000; i++) {
					// send to server
					output.write(message);
					Thread.sleep(5000);
				}

				clientSocket.close();

			} catch (IOException exc) {
				System.out.println(exc.toString());

			}

		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	byte[] readBytes(InputStream is) {
		byte b[] = new byte[1024];
		int nosRead = 0;
		try {
			nosRead = is.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte readbytes[] = Arrays.copyOfRange(b, 0, nosRead);
		return readbytes;
	}

}

public class TeltonikaClient {
	public static void main(String args[]) {

		for (int i = 0; i < 1; i++) {
			new TeltonikaClientRunnable("Thread-" + i).start();
		}

	}
}
