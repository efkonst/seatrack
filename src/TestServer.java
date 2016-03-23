class TestServer {

	public static void main(String args[]) {

		MultiThreadedServer server = new MultiThreadedServer(8888);
		new Thread(server).start();

	}
}
