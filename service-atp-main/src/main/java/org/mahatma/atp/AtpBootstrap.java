package org.mahatma.atp;

import org.helium.framework.spi.Bootstrap;

import java.io.File;

/**
 * Created by JiYunfei on 17-9-8.
 */
public class AtpBootstrap {
    public static void main(String[] args) throws Exception {
        Bootstrap.INSTANCE.addPath("service-atp-main" + File.separator + "build" + File.separator + "resources" + File.separator + "main" + File.separator + "META-INF");

        Bootstrap.INSTANCE.addPath(AtpBootstrap.class.getClassLoader().getResource("config").getPath());
        Bootstrap.INSTANCE.addPath(AtpBootstrap.class.getClassLoader().getResource("config").getPath() + "/../");
        Bootstrap.INSTANCE.initialize("bootstrap.xml");

        System.out.println("============ AtpBootstrap BOOTSTRAP START OK =================");
    }
}
