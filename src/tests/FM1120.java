package tests;

import java.util.Arrays;

public class FM1120 {

	
	
	byte[] data;
	byte codec_id;
	int recordcount;

		
	FM1120(String data) {
		this.data = hexStringToByteArray(data);
//		parseData();
		codec_id = this.data[0];

		int records=this.data[1];
		byte[] onlyrec=Arrays.copyOfRange(this.data,2,this.data.length);
		
		for(int i=0;i<records;i++){
			byte[] r1=Arrays.copyOfRange(onlyrec, (i*41), ((i+1)*41));
			System.out.println(bytesToHex(r1));
			System.out.println(parseRecord(r1));
		}
	}

	Record parseRecord(byte[] record) {

		long timestamp = getLong(Arrays.copyOfRange(record, 0, 8), 0);
		int priority = record[8];
		int longitude = getInt(Arrays.copyOfRange(record, 9, 13), 0);
		int lattitude = getInt(Arrays.copyOfRange(record, 13, 17), 0);
		int altitude = getShort(Arrays.copyOfRange(record, 17, 19), 0);
		int heading = getShort(Arrays.copyOfRange(record, 19, 21), 0);
		int sats = record[21];
		int speed = getShort(Arrays.copyOfRange(record, 22, 24), 0);
		return new Record(timestamp,priority,longitude,lattitude,altitude,heading,sats,speed);
		
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
		    return
		      ((array[offset+0] & 0xff) << 8) |
		       (array[offset+1] & 0xff);
		  }

	 final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	 public static String bytesToHex(byte[] bytes) {
	     char[] hexChars = new char[bytes.length * 2];
	     for ( int j = 0; j < bytes.length; j++ ) {
	         int v = bytes[j] & 0xFF;
	         hexChars[j * 2] = hexArray[v >>> 4];
	         hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	     }
	     return new String(hexChars);
	 }

}
