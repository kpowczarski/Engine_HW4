package objects;

public enum Events {
	
	DEATH(0),
	
	COLLISION(1)
	
	;
	
	private int code;
	
	private Events(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

}
