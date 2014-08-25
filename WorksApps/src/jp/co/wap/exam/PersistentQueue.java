package jp.co.wap.exam;

import java.util.NoSuchElementException;

/**
 * The Queue class represents an immutable first-in-first-out (FIFO) queue of objects.
 * Note: The implementation was inspired from some concepts in a book for the Scala programming language.
 * The implementation has an amortized run time of O(1) for enqueue and dequeue.
 * @param <E>
 */
public class PersistentQueue<E> {

    /*
     * The class below creates a stack data structure for the purposes of implementing an immutable queue
     * Each object is a node containing a value, the length upto that node and a pointer to the next node
     *
     * The main idea is to share objects that are created and create minimal number of objects to save memory.
     * Have one stack for enqueueing and one for dequeueing
     * 
     * When an enqueue is done a new node will be created and will point to the original queue (now represented as a stack).
     * This new node will be returned as a part of the new persistent queue object.
     * This will make sure that the original queue content has nothing added to it.
     *
     * When dequeue is done, the stack is to be inverted (to get the first insertion) and placed onto another stack.
     * After inversion, the immediate next node is returned a a part of the new persistent queue object.
     * This will make sure that the original queue content has nothing removed from it.
     */
    public class StackAsQueue<E> {

        private E nodeValue; //Value
        private StackAsQueue<E> next; //Pointer/Reference to next node
        private int lenOfStack; //Number of elements inserted till now

        /*
         * This is the default constructor and creates an empty stack
         */

        public StackAsQueue() {
            this.nodeValue = null;
            this.next = null;
            this.lenOfStack = 0;
        }

        /*
         * This constructor creates new object/node with data and points to the remainder of the stack
         */

        public StackAsQueue(E e, StackAsQueue<E> remainder) {
            this.nodeValue = e;
            this.next = remainder;
            this.lenOfStack = remainder.getlen() + 1;
        }

        /*
         * This method reurns the number of data items added
         */

        public int getlen() { //Get number of data items added
            return this.lenOfStack;
        }

        /*
         * This method adds a new node to the data stack and returns this new node as the head/top of the stack
         */
        
        public StackAsQueue<E> PushOnStack(E e) {
            return new StackAsQueue<E>(e, this);
        }

        /*
         * This method prepares the inverted stack for dequeue purposes and return this new stack.
         */
        public StackAsQueue<E> InvertStack() {
            StackAsQueue<E> newStack = new StackAsQueue<E>();
            StackAsQueue<E> currStack = this;
            while (currStack.getlen() != 0) {
                newStack = newStack.PushOnStack(currStack.nodeValue);
                currStack = currStack.next;
            }
            return newStack;
        }
    } //End of inner classs
    
    private StackAsQueue<E> enqueueStack; //Top node of stack. Enqueue on this stack
    private StackAsQueue<E> dequeueStack; //Top node of stack. Dequeue from this stack

    /*
     * requires default constructor.
     * This default constructor creates a persistent queue object which is an empty queue
     */
    public PersistentQueue() {
// modify this constructor if necessary, but do not remove default constructor
        this.enqueueStack = new StackAsQueue<E>();
        this.dequeueStack = new StackAsQueue<E>();
    }

    /**
     * This constructor creates a persistent queue object with the enqueue and dequeue stacks
     */
    public PersistentQueue(StackAsQueue<E> enqueueStack, StackAsQueue<E> dequeueStack) {
// modify or remove this constructor if necessary
        this.enqueueStack = enqueueStack;
        this.dequeueStack = dequeueStack;

    }

// add other constructors if necessary
    /**
     * Returns the queue that adds an item into the tail of this queue without modifying this queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (2, 1, 2, 2, 6) and we enqueue the value 4 into this queue,
     * this method returns a new queue (2, 1, 2, 2, 6, 4)
     * and this object still represents the queue (2, 1, 2, 2, 6) .
     * </pre>
     * If the element e is null, throws IllegalArgumentException.
     * @param e
     * @return
     * @throws IllegalArgumentException
     */

    /*
     * The method is modified to return a new PersistentQueue object in which a new node is created
     * which points to the head of the enqueue stack of the current PersistentQueue object.
     * This new node acts as the new head/top of the enqueue stack.
     * The dequeue stack is kept as it is.
     * This ensures that the original PersistentQueue object is unmodified.
     */
    
    public PersistentQueue<E> enqueue(E e) {
// TODO: make this method faster

        if (e == null) {
            throw new IllegalArgumentException("NULL is not an acceptable argument");
        }

        return new PersistentQueue<E>(this.enqueueStack.PushOnStack(e), this.dequeueStack);

        //return new PersistentQueue<E>(clone);


    }

    /**
     * Returns the queue that removes the object at the head of this queue without modifying this queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (7, 1, 3, 3, 5, 1) ,
     * this method returns a new queue (1, 3, 3, 5, 1)
     * and this object still represents the queue (7, 1, 3, 3, 5, 1) .
     * </pre>
     * If this queue is empty, throws java.util.NoSuchElementException.
     * @return
     * @throws java.util.NoSuchElementException
     */

    /*
     * The method is modified to return a new PersistentQueue object which contains the next node pointed to by the
     * head/top of the dequeue stack.
     * This new node acts as the new head of the dequeue stack.
     * If the dequeue stack is empty then the enqueue stack is inverted and replaces the dequeue stack.
     * The enqueue stack is kept empty in that case.
     * If the dequeue stack was not empty then the enqueue stack is kept as it is.
     * This ensures that the original PersistentQueue object is unmodified.
     */

    public PersistentQueue<E> dequeue() {
// TODO: make this method faster

        if (this.size() == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        if (this.dequeueStack.getlen() != 0) {
            return new PersistentQueue<E>(this.enqueueStack, this.dequeueStack.next);
        } else {
            return new PersistentQueue<E>(new StackAsQueue<E>(), this.enqueueStack.InvertStack().next);
        }
    }

    /**
     * Looks at the object which is the head of this queue without removing it from the queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (7, 1, 3, 3, 5, 1),
     * this method returns 7 and this object still represents the queue (7, 1, 3, 3, 5, 1)
     * </pre>
     * If the queue is empty, throws java.util.NoSuchElementException.
     * @return
     * @throws java.util.NoSuchElementException
     */

    /*
     * If the dequeue stack is empty then the enqueue stack is inverted onto the dequeue stack.
     * The node value of the top/head node is returned
     */
    public E peek() {
// modify this method if needed
        if (this.size() == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        if (this.dequeueStack.getlen() == 0) {
            this.PrepareForDequeue();
        }
        return this.dequeueStack.nodeValue;
    }

    /*
     * This method calls the inversion method of the stack
     */

    public void PrepareForDequeue() {
        this.dequeueStack = this.enqueueStack.InvertStack();
        this.enqueueStack = new StackAsQueue<E>();
    }

    /**
     * Returns the number of objects in this queue.
     * @return
     */
    public int size() {
// modify this method if necessary
        return (this.enqueueStack.lenOfStack + this.dequeueStack.lenOfStack);
    }
}
