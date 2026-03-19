package org.lfan142.chapter1;


import com.launchdarkly.eventsource.EventSource;

import java.util.EventListener;

public class ThisEscape {

    public ThisEscape(EventSource source){
       new testExam();
    }

    class testExam{

    }



}
