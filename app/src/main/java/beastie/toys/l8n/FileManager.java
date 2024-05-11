package beastie.toys.l8n;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private static File file;
    private final String baseFileName = "animals";
    private static ObjectMapper objectMapper;

    static FileManager instance;

    private FileManager(String format) {
        switch (format) {
            case "xml":
                file = new File(String.format("%s.xml", baseFileName));
                objectMapper = new XmlMapper();
                break;
            case "yaml":
                file = new File(String.format("%s.yaml", baseFileName));
                objectMapper = new YAMLMapper();
                break;
            default:
                file = new File(String.format("%s.json", baseFileName));
                objectMapper = new JsonMapper();
        }
    }

    public static FileManager getInstance(String format) {
        instance = new FileManager(format);
        return instance;
    }

    public <T> T read(Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void write(T t) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists() {
        return file.exists();
    }
}
