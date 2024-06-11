package com.dime.lsd.utils;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Getter;

public class LoggerListAppender extends AppenderBase<ILoggingEvent> {

    @Getter
    static private List<ILoggingEvent> events = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent eventObject) {
        events.add(eventObject);
    }

    public static void clearEventList() {
        events.clear();
    }
}
