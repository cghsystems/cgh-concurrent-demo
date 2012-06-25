package net.cghsystems.concurrent.demo.latch

class GenericDirectQueue {

    private List<GenericDirectQueueListener> listeners = []

    /**
     * Registers a {@link GenericDirectQueueListener}
     * 
     * @param listener to register
     */
    void addListener(listener) {
        //Register the listener (same as listeners.add())
        listeners << listener
    }

    /**
     * Pretends to send a message an notifies all registered listeners of its pretend success
     * 
     * @param message to send
     */
    void sendMessage(message) {
        //Notify every registered listener
        listeners.each { it.onMessageSent(message) }
    }
    
    
    
}
