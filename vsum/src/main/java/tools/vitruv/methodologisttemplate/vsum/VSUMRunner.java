package tools.vitruv.methodologisttemplate.vsum;

import java.nio.file.Path;

/**
 * 独立运行入口，方便在 IDE 中直接执行。
 */
public class VSUMRunner {

    public static void main(String[] args) {
        // Load configuration
        GaletteConfig config = new GaletteConfig();
        Path workDir = config.getWorkingPath();

        System.out.println("Running VSUM in " + workDir.toAbsolutePath());
        System.out.println("Project base path: " + config.getProjectBasePath());
        
        new Test().insertTask(workDir, 0);
    }
}
