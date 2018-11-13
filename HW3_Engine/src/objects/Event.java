package objects;

import java.io.Serializable;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	public Events type;
	
	public long timestamp;
	
	public GameObject ob1;
	
	public GameObject ob2; 
	
	public int optionalArg1;
	
	public int optionalArg2;
	
	public Event(Events type, long timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}
	
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
	
	public Event(Events type, GameObject ob1, int arg1, int arg2, long timestamp) {
		this(type, ob1, timestamp);
		optionalArg1 = arg1;
		optionalArg2 = arg2;
		
	}
	
	public int getOptionalArg1() {
		return optionalArg1;
	}

	public int getOptionalArg2() {
		return optionalArg2;
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
