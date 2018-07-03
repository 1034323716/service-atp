package test.org.mahatma.atp;

import com.feinno.threading.ExecutorFactory;
import com.feinno.threading.UpcTimerTask;

/**
 * 静态代码会一定比静态方法先执行
 * <p>
 * Created by JiYunfei on 18-3-23.
 */
public class TestUpcTimerTask {
    public static void main(String[] args) {
        UpcTimerTask.setExecutor(ExecutorFactory.newFixedExecutor("SelfExecutor", 8, 1024));
        try {
            Class.forName("com.feinno.threading.UpcTimerTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("123");
    }
}
