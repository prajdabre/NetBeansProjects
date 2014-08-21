    /**
 * 
 */
package sl.morph.mar.transducer;

/**
 * @author swapnil
 * 
 * This class represents a transition from one state to another along with output during
 * each transition 
 */
public class Transition {
	String input;
	String output;
	long destination;

        /**
         * 
         */
        public Transition() {
		super();
		input = null;
		output = null;
		destination = -1;
	}

        /**
         *
         * @param input
         * @param destination
         */
        public Transition(String input, long destination) {
		super();
		this.input = input;
		this.destination = destination;
	}

        /**
         *
         * @param input
         * @param output
         * @param destination
         */
        public Transition(String input, String output, long destination) {
		super();
		this.input = input;
		this.output = output;
		this.destination = destination;
	}

        /**
         *
         * @return
         */
        public String getInput() {
		return input;
	}

        /**
         *
         * @param input
         */
        public void setInput(String input) {
		this.input = input;
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

        /**
         *
         * @return
         */
        public long getDestination() {
		return destination;
	}

        /**
         *
         * @param destination
         */
        public void setDestination(long destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return input+"="+output+"="+destination;
	}
	
}
