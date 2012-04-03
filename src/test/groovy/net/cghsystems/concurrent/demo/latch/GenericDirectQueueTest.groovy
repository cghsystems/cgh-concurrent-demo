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
        final listener =  [ onMessageSent: { println it }  ]

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
        final expectedNumberOFMessages = 3
        final latch = new CountDownLatch(expectedNumberOFMessages)

        //Quick way of implementing the GenericDirectQueueListener interface
        final listener = [ onMessageSent: { latch.countDown() } ]
        final queue = new GenericDirectQueue()
        queue.addListener(listener)

        //Groovy way of running a cncurrent thread
        Thread.start {
            //Groovy way of executing an action x number of times
            expectedNumberOFMessages.times {
                //Send the message (the it represents the current passed from times)
                queue.sendMessage(it)
            }
        }

        assert true == latch.await(1000,TimeUnit.MILLISECONDS),
        "Expecting ${expectedNumberOFMessages} messages. Still awaiting ${latch.getCount()} messages"
    }
}
