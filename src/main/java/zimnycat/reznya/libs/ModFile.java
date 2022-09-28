package zimnycat.reznya.libs;

import zimnycat.reznya.Utilrun;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ModFile {
    private final Path path;

    public ModFile(String fileName) {
        this.path = Utilrun.path.resolve(fileName);
        try {
            if (!path.toFile().exists()) Files.createFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> readAsList() {
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public String readAsString() {
        try {
            return String.join("\n", Files.readAllLines(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void write(String content, WriteMode mode) {
        try(FileWriter fw = new FileWriter(path.toFile())) {
            String oldContent = (mode == WriteMode.APPEND ? new String(Files.readAllBytes(path)) : "");
            fw.write(oldContent + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum WriteMode {
        OVERWRITE,
        APPEND
    }
}
