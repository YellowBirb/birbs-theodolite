package yellowbirb.birbstheodolite.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import yellowbirb.birbstheodolite.BirbsTheodoliteClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfigLoader {

    private static final String MOD_ID = BirbsTheodoliteClient.MOD_ID;
    public static final File configFile = new File("./config/" + MOD_ID + "/config.json");
    public static final String backupDirPath = "./config/" + MOD_ID + "/backups";
    public static final File backupFile1 = new File("./config/" + MOD_ID + "/backups/backup1.json");
    public static final File backupFile2 = new File("./config/" + MOD_ID + "/backups/backup2.json");
    public static final File backupFile3 = new File("./config/" + MOD_ID + "/backups/backup3.json");

    public static Config loadFromFile() {
        String json = getFileString(configFile);
        if (json.isEmpty()) {
            BirbsTheodoliteClient.LOGGER.warn("Config file is either empty or could not be read, using defaults");
            Config defaults = Config.defaults();
            writeToFile(defaults, configFile);
            return defaults;
        }
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Config.ensureCompleteKeySet(jsonObject);
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, Config.class);
    }

    private static String getFileString(File file) {
        try {
            Files.createDirectories(Paths.get(backupDirPath));
            try {
                boolean newFileMade = file.createNewFile();
                try {
                    if (!newFileMade) { // File found, no new one created
                        try (Scanner scanner = new Scanner(file)) {
                            StringBuilder fileStringBuilder = new StringBuilder();
                            while (scanner.hasNext()) {
                                fileStringBuilder.append(scanner.nextLine());
                            }
                            return fileStringBuilder.toString();
                        }
                    }
                    else {
                        BirbsTheodoliteClient.LOGGER.info("No config file found, created new empty file");
                    }
                } catch (IOException e) {
                    BirbsTheodoliteClient.LOGGER.error("Failed to read config file: {}", e.getMessage());
                }
            } catch (IOException e) {
                BirbsTheodoliteClient.LOGGER.error("Failed to create config file: {}", e.getMessage());
            }
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to create config directories: {}", e.getMessage());
        }

        return "";
    }

    public static void writeToFile(Config config, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(config);

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(jsonString);
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to write config to file: {}", e.getMessage());
        }
    }

    public static void backup(Config config) {

        if (!configFile.exists()) {
            writeToFile(config, configFile);
        }

        try {
            backupFile1.createNewFile();
            backupFile2.createNewFile();
            backupFile3.createNewFile();
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to create backup files: {}", e.getMessage());
        }

        try {
            FileUtils.copyFile(backupFile2, backupFile3);
            FileUtils.copyFile(backupFile1, backupFile2);
            FileUtils.copyFile(configFile, backupFile1);
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to backup config: {}", e.getMessage());
        }
    }
}
