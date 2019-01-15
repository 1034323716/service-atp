package com.feinno.fileserver.service;

import com.feinno.fileserver.HttpFileServer;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.tag.Initializer;

@ServiceImplementation
public class ProcessServiceImpl implements ProcessService {

    @FieldSetter("${FILE_SERVER_PORT}")
    private int port;
    private String url = "/data/atp/";

    @Initializer
    public void initializer() throws Exception {
        initFileServer();
    }

    private void initFileServer() throws Exception {
        if (port == 0) {
            port = 8888;
        }
        new HttpFileServer().run(port, url);
    }
}
