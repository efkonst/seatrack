package tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;



class TeltonikaClientRunnable implements Runnable {
	private Thread t;
	private String threadName;
	static int seq = 0;


	
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

	
	
	
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	public static byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c= new byte[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}

	public static void main(String args[]) {
		
		byte[] msg=msg_gen();

		
		 System.out.println(bytesToHex(msg));
		 
		
	}
	
	public static byte[] longToBytes(long l) {
	    byte[] result = new byte[8];
	    for (int i = 7; i >= 0; i--) {
	        result[i] = (byte)(l & 0xFF);
	        l >>= 8;
	    }
	    return result;
	}
	
	public static final byte[] intToBytes(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static final byte[] shortToBytes(short value) {
	    return new byte[] {
	            (byte)(value >>> 8),
	            (byte)value};
	}

	public synchronized static int getCrc16(byte[] buffer, int offset, int bufLen, int polynom, int preset) {
		preset &= 0xFFFF;
		polynom &= 0xFFFF;
		int crc = preset;
		for (int i = 0; i < bufLen; i++) {
			int data = buffer[i + offset] & 0xFF;
			crc ^= data;
			for (int j = 0; j < 8; j++) {
				if ((crc & 0x0001) != 0) {
					crc = (crc >> 1) ^ polynom;
				} else {
					crc = crc >> 1;
				}
			}
		}
		return crc & 0xFFFF;
	}
	public static byte[] my_int_to_bb_be(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
	}

	public static byte[] gen_msg(){
		String imsi="000000000000100";
		byte[] imsibyte = imsi.getBytes();
		byte[] he={0x00,(byte)imsi.length() };
		System.out.println(bytesToHex(concat(he,imsibyte)));
		
	//	java.util.Date date= new java.util.Date();
		
		 
//		 byte[] timestamp=longToBytes(System.currentTimeMillis());
		 byte[] timestamp=longToBytes(1460147296000L);

		 byte[] prio=new byte[1];
		 prio[0] =  0;
		 
		 byte[] lon=intToBytes(237631930);
		 byte[] lat=intToBytes(380461186);
		 byte[] alt=shortToBytes((short)169);
		 byte[] head=shortToBytes((short)163);
		 byte[] sat=new byte[1];
		 sat[0] =  13;
		 byte[] speed=shortToBytes((short)0);
		 byte[] iod=new byte[6];
//		 iod[6]=1;
		
		 byte[] header={0x08,0x01};
		 byte[] footer={0x01};
//		 byte[] crcempty={(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00};

		 byte[] msg0=concat(header,timestamp);
		 
		 byte[] msg1=concat(msg0,prio);
		 byte[] msg2=concat(msg1,lon);
		 byte[] msg3=concat(msg2,lat);
		 byte[] msg4=concat(msg3,alt);
		 byte[] msg5=concat(msg4,head);
		 byte[] msg6=concat(msg5,sat);
		 byte[] msg7=concat(msg6,speed);
		 byte[] msg8=concat(msg7,iod);
		 byte[] msg9=concat(msg8,footer);
		 
		int crcgen = getCrc16(msg9, 0, msg9.length, 0xA001, 0);
		byte[] crc= intToBytes(crcgen);
		byte[] msg=concat(msg9,crc);
		 
		return msg;
		 
	}

	
	
	
	
}
