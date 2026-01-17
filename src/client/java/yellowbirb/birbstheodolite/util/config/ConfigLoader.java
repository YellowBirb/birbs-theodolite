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
    public static final File configFile = new File("./loadedConfig/" + MOD_ID + "/loadedConfig.json");
    public static final String backupDirPath = "./loadedConfig/" + MOD_ID + "/backups";
    public static final File backupFile1 = new File("./loadedConfig/" + MOD_ID + "/backups/backup1.json");
    public static final File backupFile2 = new File("./loadedConfig/" + MOD_ID + "/backups/backup2.json");
    public static final File backupFile3 = new File("./loadedConfig/" + MOD_ID + "/backups/backup3.json");

    public static Config config;

    public static void initConfig() {
        config = loadFromFile();
    }

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
        Config loadedConfig = gson.fromJson(jsonObject, Config.class);
        // TODO: writing does not work??? config.json file still does not have boxy ring colors/booleans
        writeToFile(loadedConfig, configFile);
        return loadedConfig;
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
                    else { // File not found, new one created
                        writeToFile(Config.defaults(), configFile);
                        BirbsTheodoliteClient.LOGGER.info("No loadedConfig file found, created new loadedConfig file");
                    }
                } catch (IOException e) {
                    BirbsTheodoliteClient.LOGGER.error("Failed to read loadedConfig file: {}", e.getMessage());
                }
            } catch (IOException e) {
                BirbsTheodoliteClient.LOGGER.error("Failed to create loadedConfig file: {}", e.getMessage());
            }
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to create loadedConfig directories: {}", e.getMessage());
        }

        return "";
    }

    public static void writeToFile(Config configToSave, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(configToSave);

        try (FileWriter writer = new FileWriter(file, false)) {
            System.out.println("writing");
            writer.write(jsonString);
            System.out.println("written");
        } catch (IOException e) {
            BirbsTheodoliteClient.LOGGER.error("Failed to write loadedConfig to file: {}", e.getMessage());
        }
    }

    public static void backup(Config configToBackup) {

        if (!configFile.exists()) {
            writeToFile(configToBackup, configFile);
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
            BirbsTheodoliteClient.LOGGER.error("Failed to backup loadedConfig: {}", e.getMessage());
        }
    }
}
