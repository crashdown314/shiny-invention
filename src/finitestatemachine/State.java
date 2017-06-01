package finitestatemachine;

import java.util.HashMap;
import java.util.Map;

public class State {

	private final int id;
	Map<Event, State> events;
	private FiniteStateMachine fmt;
	public State(int id){
		this.id = id;
		events = new HashMap<Event,State>();
	}
	
	public void addEvent(Event event,State state){
		events.put(event, state);
		fmt.updateEvents(event);
	}
	
	State next(Event event) {
		return events.get(event);
	}
	
	public int getId(){
		return id;
	}

	public void setFMT(FiniteStateMachine finiteStateMachine) {
		this.fmt = finiteStateMachine;		
	}
}
