import java.util.Arrays;

public class FM1120 {

	String imei;
	byte[] data;
	byte codec_id;
	int records;
	public int recordcount=0;

	public FM1120() {
	}

	public FM1120(String data) {
		this.data = hexStringToByteArray(data);
		// parseData();
		codec_id = this.data[0];

		records = this.data[1];
		byte[] onlyrec = Arrays.copyOfRange(this.data, 2, this.data.length);

		int bytesinrec=32;

		for (int i = 0; i < records; i++) {
			byte[] r1 = Arrays.copyOfRange(onlyrec, (i * bytesinrec), ((i + 1) * bytesinrec));
			System.out.println(bytesToHex(r1));
			System.out.println(parseRecord(r1));
		}
	}
	
	
	public FM1120(byte[] data,String imei) {
		this.data = data;
		// parseData();
		int bytesinrec=32;
		codec_id = this.data[0];
	
		this.recordcount =this.data[1];
		byte[] onlyrec = Arrays.copyOfRange(this.data, 2, this.data.length);

		for (int i = 0; i < this.recordcount; i++) {
			byte[] r1 = Arrays.copyOfRange(onlyrec, (i * bytesinrec), ((i + 1) * bytesinrec));
//			System.out.println(bytesToHex(r1));

			String record=parseRecord(r1).toString();
			MultiThreadedServer.logger.info(imei+":"+record);

			HttpClient htclient = new HttpClient();
					try {
						htclient.sendGet(imei+","+record);
//						htclient.sendGet(got_data_from_gps.substring(0, got_data_from_gps.length() - 1)+"%23");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			
			
		}
	}
	

//	public FM1120(byte[] d, String imeivar) {
//		this.imei = imeivar;
//		this.data = d;
//		// parseData();
//		codec_id = this.data[0];
//
//		int records = this.data[1];
//		this.recordcount = records;
//		byte[] onlyrec = Arrays.copyOfRange(this.data, 2, this.data.length);
//
//		for (int i = 0; i < records; i++) {
//			byte[] r1 = Arrays.copyOfRange(onlyrec, (i * 30), ((i + 1) * 30));
//			System.out.println(bytesToHex(r1));
//			System.out.println(imeivar + "," + parseRecord(r1).toString());
//			HttpClient htclient = new HttpClient();
//			String a = this.imei + "#" + parseRecord(r1).toString();
//			System.out.println(a);
//			try {
//				htclient.sendGet(a + "\r\n");
//				// htclient.sendGet(got_data_from_gps.substring(0,
//				// got_data_from_gps.length() - 1)+"%23");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	Record parseRecord(byte[] record) {

		long timestamp = getLong(Arrays.copyOfRange(record, 0, 8), 0);
		int priority = record[8];
		int longitude = getInt(Arrays.copyOfRange(record, 9, 13), 0);
		int lattitude = getInt(Arrays.copyOfRange(record, 13, 17), 0);
		int altitude = getShort(Arrays.copyOfRange(record, 17, 19), 0);
		int heading = getShort(Arrays.copyOfRange(record, 19, 21), 0);
		int sats = record[21];
		int speed = getShort(Arrays.copyOfRange(record, 22, 24), 0);
		byte[] data=Arrays.copyOfRange(record, 24, record.length);
//		MultiThreadedServer.logger.info("********"+bytesToHex(data));

		return new Record(timestamp, priority, longitude, lattitude, altitude, heading, sats, speed,bytesToHex(data));

	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
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

}
