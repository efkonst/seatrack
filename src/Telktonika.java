
import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.net.*;
/**
 */
public class Telktonika implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	String imei="";
	public Telktonika(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
                        clientSocket.setSoTimeout(120000);
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			long time = System.currentTimeMillis();

			byte[] readb = readBytes(input);

			// System.out.println(getImei(readb));
			imei = getImei(readb);
			MultiThreadedServer.logger.info(
					"new connection from IMEI " + imei + " IP: " + clientSocket.getRemoteSocketAddress().toString());

			boolean waitopen = true;

			if (validIMEI(imei)) {
				output.write(1);
				// acceepting data forever?
				// while (true) {

				while (waitopen) {
					byte[] newdata = readBytes(input);
					if(newdata.length==0)
					{
					 waitopen=false;
					 clientSocket.close();
					}

					MultiThreadedServer.logger.info(bytesToHex(newdata));

					int gotrecords = parseTCPTeltonika(newdata);
					output.write(my_int_to_bb_be(gotrecords));
				}

				output.close();
				input.close();
				clientSocket.close();
				// }

			} else {
				output.write(0);
				return;
			}

		} catch (IOException e) {
                     MultiThreadedServer.logger.info("Exception " +e.toString());

			System.out.println(e);
			return;
		}
	}

	int parseTCPTeltonika(byte[] tcpdata) {
		MultiThreadedServer.logger.info("read bytes: " + tcpdata.length);
		byte[] nocrc = Arrays.copyOfRange(tcpdata, 8, tcpdata.length - 4);
		int crcgen = getCrc16(nocrc, 0, nocrc.length, 0xA001, 0);
		int datacrc = getInt(Arrays.copyOfRange(tcpdata, tcpdata.length - 4, tcpdata.length), 0);

		System.out.println(crcgen + ":" + datacrc);
		FM1120 fm1120 = new FM1120(nocrc,this.imei);

		return fm1120.recordcount;

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

	String getImei(byte[] imei) {
		return new String(Arrays.copyOfRange(imei, 2, imei.length));
	}

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

	boolean validIMEI(String imei) {
		return true;
	}

	public static long getLong(byte[] array, int offset) {
		return ((long) (array[offset] & 0xff) << 56) | ((long) (array[offset + 1] & 0xff) << 48)
				| ((long) (array[offset + 2] & 0xff) << 40) | ((long) (array[offset + 3] & 0xff) << 32)
				| ((long) (array[offset + 4] & 0xff) << 24) | ((long) (array[offset + 5] & 0xff) << 16)
				| ((long) (array[offset + 6] & 0xff) << 8) | ((long) (array[offset + 7] & 0xff));
	}

	public static int getInt(byte[] array, int offset) {
		return ((array[offset] & 0xff) << 24) | ((array[offset + 1] & 0xff) << 16) | ((array[offset + 2] & 0xff) << 8)
				| (array[offset + 3] & 0xff);
	}

	public static int getShort(byte[] array, int offset) {
		return ((array[offset + 0] & 0xff) << 8) | (array[offset + 1] & 0xff);
	}

	public static byte[] my_int_to_bb_be(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
	}

}
