package objects;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class EventManager {
	
	public PriorityQueue<Event> events;
	
	public ArrayList<GameObject> objects;
	
	public EventManager(ArrayList<GameObject> g) {
		events = new PriorityQueue<Event>(10, new EventCompare());
		objects = g;
	}
	
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	
	public void handleEventsNow(long time) {
		if (events.peek() != null && events.peek().timestamp == time) {
			Event curE = events.poll();
			if (curE.type == Events.DEATH) {
				Player p = (Player) curE.ob1;
				p.handleDeathEvent();
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
