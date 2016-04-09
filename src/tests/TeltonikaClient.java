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
import java.nio.ByteBuffer;


class TeltonikaClientRunnable implements Runnable {
	private Thread t;
	private String threadName;
	static int seq = 0;
	byte[] imeipacket = { 0x00, 0x0F, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31,0x30, 0x30 };
	int initlon;
	int initlat;

	
	TeltonikaClientRunnable(String name,int initlon,int initlat) {
		threadName = name;
		this.initlon=initlon;
		this.initlat=initlat;
		// System.out.println("Creating " + threadName );
	}

	public void run() {
		System.out.println("Running " + threadName + " " );
		try {
			try {
				String sentence;
				Socket clientSocket = new Socket("127.0.0.1", 8888);
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

	//			int initlon=237631930;
	//			int initlat=380461186;
				
				seq++;
				// semd toserver
				output.write(imeipacket);
				// read from server
			
				// sentence = inFromServer.readLine();
				// System.out.println(sentence);
				byte[] newdata = readBytes(input);
				
				for (int i = 0; i < 1000; i++) {
					// send to server
					output.write(TeltonikaClient.gen_msg(initlon,initlat));
					initlon+=10000;
					initlat+=25000;
					Thread.sleep(30000);
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
	
	public static void main(String args[]) {
	int initlon=-237631930;
	int initlat=380461186;

	if( args.length==2){	
	initlon=Integer.parseInt(args[0]);
	initlat=Integer.parseInt(args[1]);
	}

	
		byte[] msg=gen_msg(initlon,initlat);
		System.out.println(bytesToHex(msg));
		
		for (int i = 0; i < 1; i++) 
			 new TeltonikaClientRunnable("Thread-" + i,initlon,initlat).start();
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

	public static byte[] gen_msg(int lon,int lat){
		return gen_msg( lon, lat,(short)169,(short)163,(byte)13,(short)0);
	}
	
	
	public static byte[] gen_msg(int lon,int lat,short alt,short angle,byte sats,short speed){
		ByteBuffer bb=ByteBuffer.allocate(45);
		
		bb.putInt(0);//lon
		bb.putInt(33);//lon
		bb.put((byte) 0x08);//codec_ID
		bb.put((byte) 0x01);//numberof data

		bb.putLong(1460147296000L);//timestamp
		bb.put((byte)0x00);//prio
		bb.putInt(lon);//lon
		bb.putInt(lat);//lat
		bb.putShort((short) alt);//alt
		bb.putShort((short) angle);//angle
		bb.put(sats);//sats
		bb.putShort(speed);//speed
		bb.put(new byte[6], 0, 6);
		bb.put((byte) 0x01);//numberof data
		
		int crcgen = getCrc16(bb.array(), 8, bb.array().length-12, 0xA001, 0);
		bb.putInt(crcgen);//numberof data

		return  bb.array();

	}
	
	
	
	
}
