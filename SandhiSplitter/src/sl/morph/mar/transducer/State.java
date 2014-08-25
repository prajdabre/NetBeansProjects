/**
 * 
 */
package sl.morph.mar.transducer;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author swapnil
 *
 *
 * This class represents a state for the transducer.
 */
public class State {
	boolean isFinal;
	Stack<Transition> transition;
	Long id;
	String output=null;

        /**
         * Default constructor
         */
        public State() {
		isFinal = false;
		transition = new Stack<Transition>();
	}

        /**
         * Constructor for creating a state may or may not be final.
         * @param isFinal
         * @param id
         */
        public State(boolean isFinal, Long id) {
		super();
		this.isFinal = isFinal;
		this.id = id;
		transition = new Stack<Transition>();
	}

        /**
         *
         * @return
         */
        public boolean isFinal() {
		return isFinal;
	}

        /**
         *
         * @param isFinal
         */
        public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

        /**
         * Returns all the null transitions from this state
         * @return
         */
        public Stack<Transition> getTransition() {
		return transition;
	}

        /**
         * Sets the null transitions from this state
         * @param transition
         */
        public void setTransition(Stack<Transition> transition) {
		this.transition = transition;
	}

        /**
         *
         * @return
         */
        public long getId() {
		return id;
	}

        /**
         *
         * @param id
         */
        public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		State s = (State) obj;
		return id.equals(s.getId());
	}
	
        /**
         * Returns transitions for a given input
         * @param s
         * @return
         */
        public Transition getTransitionForInputChar(String s) {
		for (Transition t : getTransition()) {
			if(s==null && t.getInput()==null){
				return t;
			}else if(s!=null && s.equalsIgnoreCase(t.getInput())) {
				return t;
			}
		}
		
		return null;
	} 
	
        /**
         * Sets transitions for a given input
         * @param s
         * @return
         */
        public Set<Transition> getTransitionsForInputChar(String s) {
		Set<Transition> set = new HashSet<Transition>();
		for (Transition t : getTransition()) {
			if(s==null && t.getInput()==null){
				set.add(t);
			}else if(s!=null && s.equalsIgnoreCase(t.getInput())) {
				set.add(t);
			}
		}
		if(set.isEmpty()) {
			return null;
		}else {
			return set;
		}
		
	}

        /**
         * 
         * @param s
         * @return
         */
        public static String safeTrim(String s) {
		return s!=null?s.trim():"";
	}
	
        /**
         *
         * @param previousOutput
         * @param s
         */
        public void appendOutput(String previousOutput, String s) {
			output=safeTrim(previousOutput)+safeTrim(s);
	}

        /**
         *
         * @return
         */
        public String getOutput() {
		return output;
	}

        /**
         *
         * @param output
         */
        public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return id+"-"+output+"-"+isFinal+"-";
	}

	
}
