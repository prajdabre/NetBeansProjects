/**
 * 
 */
package sl.morph.mar.transducer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

/**
 * @author swapnil
 * 
 */
public class Transducer {
	Stack<State> state = new Stack<State>();
	HashMap<Long, State> stateMap = new HashMap<Long, State>(100000); 

        /**
         * This reads in the transducer from a file and creates the states and transitions.
         * @param path
         * @throws IOException
         */
        public Transducer(String path) throws IOException {
//		path = "/home/swapnil/marathi/installationDir/src/sl/morph/mar/swap.txt";
		BufferedReader reader = new BufferedReader(
				new FileReader(path));
		String line1 = reader.readLine();
		
		while (line1 != null) {
			line1 = line1.trim();
			if (line1.matches("^final.*")) {
				// capturing final states
				String[] arr = line1.split("[\t]");
				if (arr.length > 1) {
					Long id = Long.parseLong(arr[1].trim());
					State state = new State(true, id);
					stateMap.put(id, state);
				}
			}
			else if (line1.matches(".*:<>.*")) {
				line1 = line1.replaceAll(":<>", "");
				String[] arr = line1.split("[\t]");
				if (arr.length > 2) {
					Long src = Long.parseLong(arr[0].trim());
					State st = stateMap.get(src);
					if(st == null) {
						st = new State();
						st.setId(src);
						
					}else {
						
					}
					stateMap.put(src, st);
					Long dest = Long.parseLong(arr[2].trim());
					Stack<Transition> tr = st.getTransition();
					Transition transition = new Transition(null, arr[1].trim(), dest);
					tr.push(transition);
					
				}
				
			}else if (line1.split("[\t]").length == 3){
				String[] arr = line1.split("[\t]");
				if (arr.length > 2) {
					Long src = Long.parseLong(arr[0].trim());
//					System.out.println(src.equals(0l));
					
					
					State st = stateMap.get(src);
					if(st == null) {
						st = new State();
						st.setId(src);
					}
					Long dest = Long.parseLong(arr[2].trim());
					Stack<Transition> tr = st.getTransition();
					Transition transition = new Transition(arr[1].trim(), arr[1].trim(), dest);
//					if ("".equals(arr[1].trim().replaceAll("[ \t]+", ""))) {
////						System.out.println("**************"+line1);
//						transition.setInput(null);
//					}
					tr.push(transition);
					stateMap.put(src, st);
					
				}
			}

			line1 = reader.readLine();
			
		}
		reader.close();
	}
	
	/**
	 * @deprecated
	 * @param inputString
	 * @param outFile
	 * @return
	 */
	public String analyze_string(String inputString, File outFile) {
		Long currentState = 0l;
		char[] chars = inputString.toCharArray();
		Stack<State> openStates = new Stack<State>();
		
		openStates.push(stateMap.get(currentState));
		
		for (char c : chars) {
			Stack<State> newStates = new Stack<State>();
			while(!openStates.isEmpty()) {
				
				currentState = openStates.pop().getId();
				State currState = stateMap.get(currentState);
				if(currState==null) {
					System.out.println("State Not Found :"+currentState);
					continue;
				}
				Transition transition = currState.getTransitionForInputChar(null);
				State destState;
				if(transition!=null) {
					destState = stateMap.get(transition.getDestination());
					destState.appendOutput(currState.getOutput(),transition.getOutput());
					openStates.push(destState);
				}
				
				transition = currState.getTransitionForInputChar(" ");
				if(transition!=null) {
					destState = stateMap.get(transition.getDestination());
					destState.appendOutput(currState.getOutput(),transition.getOutput());
					openStates.push(destState);
				}
			
				
				transition = currState.getTransitionForInputChar(c+"");
				
				if (transition!=null) {
					destState = stateMap.get(transition.getDestination());
					destState.appendOutput(currState.getOutput(),transition.getOutput());
					newStates.push(destState);
				}else {
//					System.out.println("No transition for char"+c+"---"+currState);
				}
					
				
			}
			openStates = newStates;
		}
//		System.out.println(openStates);
		String stem = inputString;
		for (State state : openStates) {
			System.out.println(state.getOutput());
			stem = state.getOutput();
			break;
		}
		return stem;
	}
	
	/**
         * This parses the given input and gives the all the possible splits of the word
	 * @param inputString
	 * @return analyses for input string
	 */
	public Vector<String> getParsedOutput(String inputString) {
		Vector<String> output = new Vector<String>();
		
//		Initialising state to state 0
		Long currentState = 0l;
		char[] chars = inputString.toCharArray();
		Stack<State> openStates = new Stack<State>();
		State currState = getClone(currentState);
		
		openStates.push(currState);
		
		// Expanding null transitions for initial state
		Set<Transition> transtns = currState.getTransitionsForInputChar(null);
		State destState;
		if (transtns!=null) {
			for (Transition trans : transtns) {
				destState = getClone(trans.getDestination());
				destState.appendOutput(currState.getOutput(),trans.getOutput());
				openStates.push(destState);
			}
		}
		
		// iterating through input characters
		for (char c : chars) {
			Stack<State> newStates = new Stack<State>();
			while(!openStates.isEmpty()) {
				
				currState = openStates.pop();
				
				Set<Transition> transitions = currState.getTransitionsForInputChar(c+"");
				destState = null;
				if (transitions!=null) {
					for (Transition trans : transitions) {
						destState = getClone(trans.getDestination());
						destState.appendOutput(currState.getOutput(),trans.getOutput());
						newStates.push(destState);
					}
				}
				
				// Expanding null transitions recursively
				if (destState != null) {
					Stack<State> nullStates = new Stack<State>();
					nullStates.push(destState);
					while (!nullStates.isEmpty()) {
						destState = nullStates.pop();
						State newDest;
						transitions = destState.getTransitionsForInputChar(null);
						// State destState;
						if (transitions != null) {
							for (Transition trans : transitions) {
								newDest = getClone(trans.getDestination());
								newDest.appendOutput(destState.getOutput(),
										trans.getOutput());
								nullStates.push(newDest);
								newStates.push(newDest);
							}
						}
					}

				}
				
			}
			// making newly discovered states as not open
			openStates = newStates;
		}
//		System.out.println(openStates);
		String stem = "no analysis for " +inputString;
		for (State state : openStates) {
			if (state.isFinal) {
				stem = state.getOutput();
				output.add(stem);
//				System.out.println(state.getOutput());
			}
		}
		if(output.size()==0) {
//			System.out.println(stem);
			output.add(stem);
		}
		return output;
	}
	
	/**
	 * @param id state Id
	 * @return a copy of state present in global state table with given ID
	 */
	public State getClone(Long id) {
		State state = new State();
		State temp = stateMap.get(id);
		state.setFinal(temp.isFinal());
		state.setId(temp.getId());
		state.setOutput(temp.getOutput());
		state.setTransition(temp.getTransition());
		
		return state;
		
		
	}
}
