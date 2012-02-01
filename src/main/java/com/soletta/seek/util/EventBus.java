package com.soletta.seek.util;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;


public class EventBus implements EventBusMXBean {
    
    private final static EventBus PLATFORM = new EventBus();
    private final static AtomicInteger BUS_NUMBER = new AtomicInteger();
    
    private final LinkedBlockingDeque<Object> queue = new LinkedBlockingDeque<Object>();
    private final ConcurrentHashMap<Key, BusSubscription> subscribers = new ConcurrentHashMap<EventBus.Key, EventBus.BusSubscription>();
    
    private final Memoizer<Object, Key> keyMemo = new Memoizer<Object, EventBus.Key>(new Computable<Object, Key>(){
        @Override
        public Key compute(Object listener) throws InterruptedException, ExecutionException {
            return new Key();
        }});
    private final Memory<String, ObjectName> nameMemo = new Memory<String, ObjectName>(new Calculation<String, ObjectName>() {
        @Override
        public ObjectName compute(String name) {
            try {
                return ObjectName.getInstance(name);
            } catch (MalformedObjectNameException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
    });
    
    private MBeanServer eventServer;
    private Executor exec;
    private ObjectName objectName;
    private StandardEmitterMBean mbean;
    
    public static EventBus getPlatformEventBus() {
        return PLATFORM;
    }

    public EventBus() {
        
        try {
            this.objectName = ObjectName.getInstance("Soletta", "EventBus", Integer.toString(BUS_NUMBER.incrementAndGet()));
            this.exec = Executors.newSingleThreadExecutor();
            init(ManagementFactory.getPlatformMBeanServer());
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
    
    public EventBus(MBeanServer mbeanServer, ObjectName objectName, Executor exec) {
        this.objectName = objectName;
        this.exec = exec;
        init(mbeanServer);
    }
    
    private void init(MBeanServer mbeanServer) {
        NotificationBroadcasterSupport emitter = new NotificationBroadcasterSupport(exec);
        mbean = new StandardEmitterMBean(this, EventBusMXBean.class, true, emitter);
        eventServer = MBeanServerFactory.createMBeanServer("bus");
    }

    public Subscription subscribe(Object listener) {
        return subscribe(null, listener);
    }
    
    public Subscription subscribe(String topic, Object listener) {
        if (topic == null || topic.isEmpty())
            topic = "*:*";
        ObjectName topicName = nameMemo.compute(topic);
        
//        topicName.
//        
//        ManagementFactory
        Key key;
        try {
            key = analyze(listener);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Subscription() {
                @Override
                public void cancel() {
                }};
        }
        BusSubscription subscription = new BusSubscription(key, listener);
        subscribers.put(key, subscription);
        return subscription;
    }
    
    private Key analyze(Object listener) throws InterruptedException {
        try {
            return keyMemo.compute(listener);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void publish(Object event) {
        publish(false, event);
    }

    /** Publish an event, optionally to the front of the event queue.
     * 
     * @param frontOfQueue
     * @param event
     */
    public void publish(boolean frontOfQueue, Object event) {
        if (frontOfQueue)
            queue.addFirst(event);
        else
            queue.addLast(event);
    }
    
    class BusSubscription implements Subscription {

        private final Object listener;
        private final Key key;

        BusSubscription(Key key, Object listener) {
            this.key = key;
            this.listener = listener;
        }

        @Override
        public void cancel() {
            subscribers.remove(key);
        }
        
    }
    
    public interface SubscriberMXBean {
        
    }
    
    class Key {
        
    }
    
}


