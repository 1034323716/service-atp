package org.mahatma.atp.common.stack.spi;

import com.feinno.urcs.http.HttpClientApi;
import org.mahatma.atp.common.annotations.TestStack;
import org.mahatma.atp.common.annotations.TestVarSetter;
import org.mahatma.atp.common.stack.ModuleStack;
import org.mahatma.atp.common.type.StackType;
import org.mahatma.atp.common.type.StackTypec;

@TestStack(id = StackTypec.HTTP)
public class HttpStack implements ModuleStack {

    private HttpClientApi api;

    @Override
    public void start() throws Exception {
        api = HttpClientApi.getInstance("HttpClient");
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public HttpClientApi getStack() {
        return api;
    }
}
