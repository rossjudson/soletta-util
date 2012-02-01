package com.soletta.seek.util;

import java.util.concurrent.ThreadFactory;

public interface GroupThreadFactory extends ThreadFactory {
    ThreadGroup getThreadGroup();
}
