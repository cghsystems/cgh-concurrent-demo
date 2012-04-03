package net.cghsystems.concurrent.demo.latch

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import org.junit.Test



class GenericDirectQueueTest {

    /**
     * Demonstrates that when a message is sent to a synchronous queue that it is received and the 
     * {@link GenericDirectQueueListener#onMessageSent(Object)} is notified
     */
    @Test
    void demonstrateSimpleMessagingStructure() {
        final expectedMessage = "Hello"
        final listener =  [ onMessageSent: { assert it == expectedMessage }  ]

        def queue = new GenericDirectQueue()
        queue.addListener(listener)

        queue.sendMessage(expectedMessage)
    }

    /**
     * Demonstrates that when a number of messages are sent to an asynchronous queue that they are received and the 
     * {@link GenericDirectQueueListener#onMessageSent(Object)} is notified
     */
    @Test
    void demostrateLatchUsage() {
        final expectedNumberOfMessages = 3
        final latch = new CountDownLatch(expectedNumberOfMessages)

        //Quick way of implementing the GenericDirectQueueListener interface
        final listener = [ onMessageSent: { latch.countDown() } ]
        final queue = new GenericDirectQueue()
        queue.addListener(listener)

        //Groovy way of running a concurrent thread
        Thread.start {
            //Groovy way of executing an action x number of times
            expectedNumberOfMessages.times {
                //Send the message (the it represents the current passed from times)
                queue.sendMessage(it)
            }
        }

        //Its important to check the result of latch.await(o,o) as in not doing so you cannot be 100% that you received all
        //of the messages you were hoping for
        assert true == latch.await(1000,TimeUnit.MILLISECONDS),
        "Expecting ${expectedNumberOfMessages} messages. Still awaiting ${latch.getCount()} messages"
    }
}
