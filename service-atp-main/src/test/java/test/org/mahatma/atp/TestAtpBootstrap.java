package test.org.mahatma.atp;

import org.helium.framework.spi.Bootstrap;

/**
 * Created by JiYunfei on 17-9-8.
 */
public class TestAtpBootstrap {
    public static void main(String[] args) throws Exception {
        Bootstrap.INSTANCE.addPath("service-atp-main/src/main/resources/META-INF");
        Bootstrap.INSTANCE.addPath(TestAtpBootstrap.class.getClassLoader().getResource("config").getPath());
        Bootstrap.INSTANCE.initialize("bootstrap.xml");
    }
}
