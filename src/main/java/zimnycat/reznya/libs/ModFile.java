package zimnycat.reznya.libs;

import zimnycat.reznya.Utilrun;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        return new ArrayList<>();
    }

    public String readAsString() {
        try {
            StringBuilder builder = new StringBuilder();
            Files.readAllLines(path).forEach(builder::append);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void write(String content, WriteMode mode) {
        try {
            String oldContent = (mode == WriteMode.APPEND ? new String(Files.readAllBytes(path)) : "");
            FileWriter fw = new FileWriter(path.toFile());
            fw.write(oldContent + content);
            fw.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public enum WriteMode {
        OVERWRITE,
        APPEND
    }
}
