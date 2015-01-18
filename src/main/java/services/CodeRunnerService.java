package services;

import com.coderunner.CodeRun;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhinavn on 18/1/15.
 */

@Component
public class CodeRunnerService {

    private static Logger logger = Logger.getLogger(CodeRunnerService.class);
    private static final String CLASS_TO_RUN = "ClassToRun";
    private static final Random randomDirGenerator = new Random();
    private static final CodeRun badRun = new CodeRun();

    private String createClass(String code) throws IOException {
        // Create a random directory to handle code run. Randomness avoids to some extent overlap between multiple runs.
        String newDir = "/tmp/" + randomDirGenerator.nextInt(1000);
        File tempDir = new File(newDir);
        if(!tempDir.exists()){
            logger.info("Creating new dir:" + newDir);
            tempDir.mkdirs();
        } else {
            logger.info("Cleaning existing dir:" + newDir);
            for(File file: tempDir.listFiles())
                file.delete();
        }
        File classFile = new File(newDir + "/" + CLASS_TO_RUN+".java");

        // Write the submitted code to a .java file in the new directory
        try(BufferedWriter classFileWriter = new BufferedWriter(new FileWriter(classFile));){
            classFileWriter.write(code);
        }
        return newDir;
    }

    private CodeRun runCommand(String command) throws IOException, InterruptedException {
        StringBuffer output = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Process run = Runtime.getRuntime().exec(command);
        try(BufferedReader outputReader = new BufferedReader(new InputStreamReader(run.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(run.getErrorStream()));
        ){
            // Run command with a timeout of a minute, to avoid running a thread infinitely in case of malicious code
            if(!run.waitFor(1, TimeUnit.MINUTES)){
                run.destroy();
                logger.error("Execution didn't finish in a minute, exiting");
                return badRun;
            }

            String line;
            while((line = outputReader.readLine()) != null)
                output.append(line).append("\n");

            while((line = errorReader.readLine()) != null)
                error.append(line).append("\n");
        }

        CodeRun result = new CodeRun();
        result.setExitVal(run.exitValue());
        result.setOutput(output.toString());
        result.setError(error.toString());
        return result;
    }

    public CodeRun compileAndRun(String code){
        String codeDir = null;
        badRun.setOutput("Looks like the server failed to complete your request.");
        try {
            codeDir = createClass(code);
            logger.debug("CodeDir:" + codeDir);
            // First try to compile. If compilation fails return the failure, else run the class.
            CodeRun compileRun = runCommand("javac " + codeDir + "/" + CLASS_TO_RUN  + ".java");
            if(compileRun.getExitVal() != 0){
                logger.info("Compilation failed");
                return compileRun;
            }else {
                logger.info("Compilation succeeded");
                return runCommand("java -cp " + codeDir + " " +CLASS_TO_RUN);
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Exception while running code", e);
            return badRun;
        } finally {
            File tempDir = new File(codeDir);
            for(File file: tempDir.listFiles())
                file.delete();
            tempDir.delete();
        }
    }

}
