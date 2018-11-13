package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;

import main.Server;

public class EventManager implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public PriorityQueue<Event> events;
	
	public PriorityQueue<Event> recordBuffer;
	
	//public ArrayList<GameObject> objectsInitialState;
	//public ArrayList<GameObject> objectsFinalState;
	
	public boolean recording = false;
	
	public EventManager(ArrayList<GameObject> g) {
		events = new PriorityQueue<Event>(10, new EventCompare());
		recordBuffer = new PriorityQueue<Event>(50, new EventCompare());
		//objectsInitialState = new ArrayList<GameObject>();
		//objectsFinalState = new ArrayList<GameObject>();
		recording = false;
	}
	
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void addEventToBuffer(Event e) {
		recordBuffer.add(e);
	}
	
	
	public void handleEventsNow(long time) {
		while (events.peek() != null && events.peek().timestamp <= time) {
			Event curE = events.poll();
			if (recording) {
				curE.timestamp = Server.replayTime.getCurrentTime();
				addEventToBuffer(curE);
				System.out.println("Added Event to buffer");
			}
			if (curE.type == Events.DEATH) {
				Player p = (Player) curE.ob1;
				p.handleDeathEvent();
			}
			else if (curE.type == Events.SPAWN) {
				Player pS = (Player) curE.ob1;
				pS.handleSpawnEvent();
				Server.game_objects.add(pS);
			}
			else if(curE.type == Events.COLLISION) {
				curE.ob1.handleCollision(curE.ob2);
			}
			else if(curE.type == Events.MOVEMENT) {
				curE.ob1.handleMovement(curE.optionalArg1, curE.optionalArg2);
			}
			else if(curE.type == Events.RECORD) {
				recording = true;
				Server.replayTime = new Timeline(15, Server.time);
				curE.timestamp = Server.replayTime.getCurrentTime();
				addEventToBuffer(curE);
				Server.recording = 1;
				for ( int i = 0; i < Server.game_objects.size(); i++ ) {
                    Server.game_objects.get(i).setReplay();
                }
			}
		}
	}
	
	
//	public static void main(String args[]) {
//		ArrayList<GameObject> g = new ArrayList<GameObject>();
//		EventManager e = new EventManager(g);
//		Event e1 = new Event(Events.DEATH, 1, 123);
//		Event e2 = new Event(Events.COLLISION, 2, 123);
//		Event e3 = new Event(Events.DEATH, 3, 123);
//		Event e4 = new Event(Events.COLLISION, 4, 124);
//		e.addEvent(e2);
//		e.addEvent(e1);
//		e.addEvent(e3);
//		e.addEvent(e4);
//		e.events.poll();
//		e.events.poll();
//	}

}
