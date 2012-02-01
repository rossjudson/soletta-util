package test.util;

import org.junit.Test;

import com.soletta.seek.util.EventHandler;

public class EventBusTest {

    @Test
    public void testSend() {
        
    }
    
    @EventHandler(markers=TestEventKey.class)
    static public class Sender {
        
    }
    
    static public class MyEvent {
        
    }
    
    @EventHandler(markers=TestEventKey.class)
    static public class TestListener {

        @EventHandler
        public void myEvent(MyEvent event) {
            
        }

        // Receives events in the "dom" domain
        @EventHandler(key="dom:*")
        public void someEvents(MyEvent event) {
            
        }
        
        @TestEventKey("keyed")
        public void keyedEvent(MyEvent event) {
            
        }
    }
    
    static public interface MyListener {
        void handle(MyEvent event);
    }
}
