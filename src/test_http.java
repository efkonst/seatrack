
public class test_http {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient htclient=new HttpClient();
		System.out.println("Sending Data");
		try {
			htclient.sendGet("test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Data sent");
		
	}

}
