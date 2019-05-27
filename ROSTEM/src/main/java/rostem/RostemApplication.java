package rostem;

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RostemApplication {

	public static void main(String[] args) {
        deleteOldestLogFile("./logs");
	    SpringApplication.run(RostemApplication.class, args);
	}

    /*
    This method deletes the oldest log file.
    */
    public static void deleteOldestLogFile(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles();

        if (files != null && files.length == 5) {
            long oldest = Long.MAX_VALUE;
            File fileToDelete = null;
            for (File file : files) {
                if (file.lastModified() < oldest) {
                    fileToDelete = file;
                    oldest = file.lastModified();
                }
            }
            fileToDelete.delete();
        }
    }
}

