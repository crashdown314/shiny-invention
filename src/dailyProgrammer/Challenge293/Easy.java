package dailyProgrammer.Challenge293;

import java.util.Arrays;

import finitestatemachine.Event;
import finitestatemachine.FiniteStateMachine;
import finitestatemachine.State;

public class Easy {

	public static void main(String[] args) {
		if (args.length<1){
			System.out.println("USAGE: easy|medium colors...");
			System.exit(0);
		}
		String[] arguments = Arrays.copyOfRange(args, 1, args.length);
		if ("easy".equals(args[0])){
			easy(arguments);
		}else if ("medium".equals(args[0])){
			medium(arguments);
		}
	}
	
	private static void easy(String[] args){

		FiniteStateMachine fmt = buildEasy();
		boolean defused = true;
		for (int i = 0;i<args.length;i++){
			System.out.println(args[i]);
			int next = parse(args[i]);
			fmt.next(next);
			defused &= fmt.getCurrentStateID()<0;
		}
		if (defused)
			System.out.println("Bomb defused");
		else
			System.out.println("Boom");
		
	}
	
	private static void medium(String[] args){
		FiniteStateMachine fmt = buildMedium();
		boolean defused = false;
		for (String s : args){
			int eventId = parse(s);
			fmt.next(eventId);
			if (fmt.getCurrentStateID()<0) break;
			if (fmt.getCurrentStateID()==6){
				defused = true;
				break;
			}
		}
		System.out.println(defused?"defused":"Boom");
	}
	
	static FiniteStateMachine buildMedium(){
		FiniteStateMachine fmt = new FiniteStateMachine();
		State s0 = new State(0);
		State s1 = new State(1);
		State s2 = new State(2);
		State s3 = new State(3);
		State s4 = new State(4);
		State s5 = new State(5);
		State exit = new State(6);
		
		fmt.addState(s0);
		fmt.addState(s1);
		fmt.addState(s2);
		fmt.addState(s3);
		fmt.addState(s4);
		fmt.addState(s5);
		fmt.addState(exit);
		
		Event white = new Event(0);
		Event red = new Event(1);
		Event black = new Event(2);
		Event orange = new Event(3);
		Event green = new Event(4);
		
		s0.addEvent(white, s1);
		s0.addEvent(red, s2);
		
		s1.addEvent(white, s2);
		s1.addEvent(orange, s3);
		
		s2.addEvent(red, s0);
		s2.addEvent(black, s3);
		
		s3.addEvent(black, s3);
		s3.addEvent(orange, s4);
		s3.addEvent(green, s5);
		
		s4.addEvent(green, exit);
		
		s5.addEvent(orange, exit);
		
		return fmt;
	}
	
	static FiniteStateMachine buildEasy(){
		FiniteStateMachine fmt = new FiniteStateMachine();
		State s0 = new State(0);
		State sW = new State(1);
		State sR = new State(2);
		State sB = new State(3);
		State sO = new State(4);
		State sG = new State(5);
		State sP = new State(6);
		
		fmt.addState(s0);
		fmt.addState(sW);
		fmt.addState(sR);
		fmt.addState(sB);
		fmt.addState(sO);
		fmt.addState(sG);
		fmt.addState(sP);
		
		
		Event white = new Event(0);
		Event red = new Event(1);
		Event black = new Event(2);
		Event orange = new Event(3);
		Event green = new Event(4);
		Event purple = new Event(5);
		
		s0.addEvent(white, sW);
		s0.addEvent(red, sR);
		s0.addEvent(black, sB);
		s0.addEvent(orange, sO);
		s0.addEvent(green, sG);
		s0.addEvent(purple, sP);
		
		sW.addEvent(red, sR);
		sW.addEvent(orange, sO);
		sW.addEvent(green, sG);
		sW.addEvent(purple, sP);
		
		sR.addEvent(green, sG);
		
		sB.addEvent(red, sG);
		sB.addEvent(black, sB);
		sB.addEvent(purple, sP);
		
		sO.addEvent(red, sR);
		sO.addEvent(black, sB);
		
		sG.addEvent(orange, sO);
		sG.addEvent(white,sW);
		
		sO.addEvent(red, sR);
		sO.addEvent(black, sB);
		
		

		return fmt;
	}
	
	static int parse(String toParse){
		if ("white".equals(toParse)) return 0;
		if ("red".equals(toParse)) return 1;
		if ("black".equals(toParse)) return 2;
		if ("orange".equals(toParse)) return 3;
		if ("green".equals(toParse)) return 4;
		if ("purple".equals(toParse)) return 5;
		return 6;
	}
}
