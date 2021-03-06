package tests;

public class FM1120parser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FM1120 fm1120=new FM1120("080400000113fc208dff000f14f650209cca80006f00d60400040004030101150316030001460000015d0000000113fc17610b000f14ffe0209cc580006e00c00500010004030101150316010001460000015e0000000113fc284945000f150f00209cd200009501080400000004030101150016030001460000015d0000000113fc267c5b000f150a50209cccc0009300680400000004030101150016030001460000015b0004");
		
/*		for (int i=0;i<fm1120.data.length;i++)
			System.out.println(String.format("%02x", fm1120.data[i]));
*/
		
/*		int i = ((0xFF & fm1120.data[0]) << 24) | ((0xFF & fm1120.data[1]) << 16) |
	            ((0xFF & fm1120.data[2]) << 8) | (0xFF & fm1120.data[3]);
*/
		
//		System.out.println(fm1120.speed);
		
		
	}
	
	public static int getCrc16(Byte[] buffer) {
	    return getCrc16(buffer, 0, buffer.length, 0xA001, 0);
	    }

	public synchronized static int getCrc16(Byte[] buffer, int offset, int bufLen, int polynom, int preset) {
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

}
