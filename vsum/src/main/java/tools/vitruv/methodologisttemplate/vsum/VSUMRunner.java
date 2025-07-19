package tools.vitruv.methodologisttemplate.vsum;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 独立运行入口，方便在 IDE 中直接执行。
 */
public class VSUMRunner {

    public static void main(String[] args) {
        // 不再需要 Test.setup() 注册 XMI 工厂
        Path workDir = Paths.get("galette-output-0");

        System.out.println("Running VSUM in " + workDir.toAbsolutePath());
        new Test().insertTask(workDir, 0);
    }
}
