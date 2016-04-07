public class Record {
	long timestamp;
	int priority;
	float longitude;
	float lattitude;
	int altitude;
	int heading;
	int sats;
	int speed;

	Record(long t, int pr, int lon, int lat, int alt, int head, int sat, int spd) {
		this.timestamp = t;
		this.priority = pr;
		this.longitude = lon;
		this.lattitude = lat;
		this.altitude = alt;
		this.heading = head;
		this.sats = sat;
		this.speed = spd;

	}

	public String toString(){
		
	/*	return "time   : "+this.timestamp+"\r\n"+
  			   "prio   : "+this.priority+"\r\n"+
  			   "long   : "+this.longitude/10000000+"\r\n"+
  			   "latt   : "+this.lattitude/10000000+"\r\n"+
  			   "alt    : "+this.altitude+"\r\n"+
  			   "heading: "+this.heading+"\r\n"+
  			   "sats   : "+this.sats+"\r\n"+
  			   "speed  : "+this.speed+"\r\n";
	*/
	
	return this.timestamp+","+this.longitude/10000000+","+this.lattitude/10000000+","+this.altitude+","+this.heading+","+this.sats+","+this.speed;
	
	}
	
	

}
