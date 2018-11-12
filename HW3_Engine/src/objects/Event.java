package objects;

import java.io.Serializable;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	public Events type;
	
	public long timestamp;
	
	public GameObject ob1;
	
	public GameObject ob2; 
	
	public Event(Events type, GameObject ob, long timestamp) {
		this.type = type;
		this.timestamp = timestamp;
		this.ob1 = ob;
	}
	
	public Event(Events type, GameObject ob1, GameObject ob2, long timestamp) {
		this.type = type;
		this.timestamp = timestamp;
		this.ob1 = ob1;
		this.ob2 = ob2;
	}
	
	public GameObject getObject1() {
		return ob1;
	}
	
	public GameObject getObject2() {
		return ob2;
	}
	
	public long getTime() {
		return timestamp;
	}
	
	public Events getType() {
		return type;
	}
	
}
