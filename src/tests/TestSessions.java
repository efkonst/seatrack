package tests;

import java.net.*;
import java.io.*;


class RunnableDemo implements Runnable {
   private Thread t;
   private String threadName;
   static int seq=0;
   
   RunnableDemo( String name){
       threadName = name;
   //    System.out.println("Creating " +  threadName );
   }
   public void run() {
      System.out.println("Running " +  threadName );
      try {
		  try{
			String sentence;
			Socket clientSocket = new Socket("192.168.1.50", 8888);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			seq++;
			outToServer.writeBytes("*HQ,00000000000000"+seq+",V1,215125,A,4047.98644,N,02202.70738,E,18.44,60,210316,EFE7FBFF#\r\n");
            sentence = inFromServer.readLine();
			System.out.println(sentence);
			
			for(int i=0;i<1000;i++){
			outToServer.writeBytes("*HQ,00000000000000"+seq+",V1,215125,A,4047.98644,N,02202.70738,E,18.44,60,210316,EFE7FBFF#\r\n");
            Thread.sleep(100);
			}
			clientSocket.close();
			
		  }
		  catch (IOException exc){
			  System.out.println(exc.toString());
			  
		  }
         
     } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
     }
     System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start ()
   {
      System.out.println("Starting " +  threadName );
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}

public class TestSessions {
   public static void main(String args[]) {
   
		for(int i=0;i<200;i++){
      new RunnableDemo( "Thread-"+i).start();
	  }
      
   }   
}
