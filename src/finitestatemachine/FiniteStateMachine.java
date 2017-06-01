package finitestatemachine;

import java.util.HashSet;
import java.util.Set;

public class FiniteStateMachine {
	private Set<State> states;
	private Set<Event> events;
	private State current;
	
	public FiniteStateMachine(){
		states = new HashSet<State>();
		events = new HashSet<Event>();
	}
	
	public void addState(State toAdd){
		toAdd.setFMT(this);
		states.add(toAdd);
		if (states.size()==1)
			current = toAdd;
		updateEvents(toAdd);
	}
	
	void updateEvents(State state){
		for (Event e : state.events.keySet())
			events.add(e);
	}
	
	void updateEvents(Event event){
		events.add(event);
	}
	
	public void next(int eventId){
		for (Event e : events){
			if (e.id == eventId){
				next(e);
				return;
			}
		}
	}
	
	public int getCurrentStateID(){
		if (current==null) return -1;
		return current.getId();
	}
	
	
	public void next(Event event){
		if (current==null) return;
		current = current.next(event);
	}
	
}
