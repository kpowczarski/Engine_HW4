package objects;

public enum Events {
	
	COLLISION(0),
	
	DEATH(1),
	
	SPAWN(2),
	
	MOVEMENT(3),
	
	RECORD(4)
	
	;
	
	private int code;
	
	private Events(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

}
