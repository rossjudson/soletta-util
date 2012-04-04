package test.util;

import org.junit.Test;

import com.soletta.seek.util.EventHandler;

/**
 */
public class EventBusTest {

    @Test
    public void testSend() {

    }

    /**
     */
    @EventHandler(markers = TestEventKey.class)
    static public class Sender {

    }

    /**
     */
    static public class MyEvent {

    }

    /**
     */
    @EventHandler(markers = TestEventKey.class)
    static public class TestListener {

        /**
         * Method myEvent.
         * 
         * @param event
         *            MyEvent
         */
        @EventHandler
        public void myEvent(MyEvent event) {

        }

        // Receives events in the "dom" domain
        /**
         * Method someEvents.
         * 
         * @param event
         *            MyEvent
         */
        @EventHandler(key = "dom:*")
        public void someEvents(MyEvent event) {

        }

        /**
         * Method keyedEvent.
         * 
         * @param event
         *            MyEvent
         */
        @TestEventKey("keyed")
        public void keyedEvent(MyEvent event) {

        }
    }

    /**
     */
    static public interface MyListener {
        /**
         * Method handle.
         * 
         * @param event
         *            MyEvent
         */
        void handle(MyEvent event);
    }
}
