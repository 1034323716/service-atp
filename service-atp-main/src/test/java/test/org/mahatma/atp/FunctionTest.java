package test.org.mahatma.atp;

import org.helium.framework.BeanIdentity;
import org.helium.framework.spi.Bootstrap;
import org.junit.Before;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.common.annotations.Test;
import org.mahatma.atp.common.db.bean.TaskResult;

import java.util.Date;

/**
 * Created by lyfx on 17-10-11.
 */
public class FunctionTest {

    @Before
    public void setup() throws Exception {
        Bootstrap.INSTANCE.addPath("atp-main/src/main/resources/META-INF");
        Bootstrap.INSTANCE.addPath(TestAtpBootstrap.class.getClassLoader().getResource("config").getPath());
        Bootstrap.INSTANCE.initialize("bootstrap.xml",false,false);
    }

    @Test
    public void testStoreLog(){
        TaskLogStore service = (TaskLogStore) Bootstrap.INSTANCE.getBean((new BeanIdentity("atp", "TaskLogStore"))).getBean();
        TaskResult taskResult = new TaskResult();
        taskResult.setCreateTime(new Date());
        taskResult.setTaskId(1L);
        long taskResult1 = service.createTaskResult(taskResult);
        System.out.println(taskResult1);

    }

    @Test
    public void updateTaskStoreLog(){
        TaskLogStore service = (TaskLogStore) Bootstrap.INSTANCE.getBean((new BeanIdentity("atp", "TaskLogStore"))).getBean();
        TaskResult taskResult = new TaskResult();
        taskResult.setCreateTime(new Date());
        taskResult.setTaskId(1L);
        taskResult.setId(3L);
        taskResult.setCode("200");
        taskResult.setDesc("test");
        taskResult.setFinishTime(new Date());
        taskResult.setState(1);


        service.updateTaskResult(taskResult);
//        System.out.println(taskResult1)

    }




    public static void main(String[] args) throws Exception {
        FunctionTest functionTest = new FunctionTest();
        functionTest.setup();
        functionTest.updateTaskStoreLog();
    }

}
