package test.org.mahatma.atp.common;

import org.mahatma.atp.common.engine.AutoTestEngine;

public class TestMain {


    public static void main(String[] args) {
        AutoTestEngine.INSTANCE.addPropertiesPath("schedule-api/src/test/resources/params.properties");

        AutoTestEngine.INSTANCE.addModule(FirstTest.class);
//        AutoTestEngine.INSTANCE.runAll();

//        AutoTestEngine.INSTANCE.start();

    }
}
