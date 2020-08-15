package co.uk.isxander.socialbot;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class Variables {

    public static String getData(int index, String serverId) {
        String path = "./config/servers/" + serverId + ".cfg";
        List<String> allValues;

        try {
            allValues = Files.readAllLines(Paths.get(path));
        }
        catch (FileNotFoundException | NoSuchFileException e) {
            File file = new File(path);
            try {
                if (file.createNewFile()) {
                    setData(index, "null", serverId);
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return allValues.get(index);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void setData(int index, String data, String serverId) {
        String path = "./config/servers/" + serverId + ".cfg";
        List<String> allValues;

        try {
            allValues = Files.readAllLines(Paths.get(path));
        }
        catch (FileNotFoundException | NoSuchFileException e) {
            File file = new File(path);
            try {
                if (file.createNewFile()) {
                    System.out.println("Created new file.");
                    setData(index, data, serverId);
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int preSize = allValues.size();
        if (allValues.size() < index + 1) {
            for (int i = 0; i < index + 1 - preSize; i++) {
                allValues.add(null);
            }
        }
        System.out.println(index);
        allValues.set(index, data);

        try (FileWriter fileWriter = new FileWriter(path)) {
            for (String allValue : allValues) {
                fileWriter.write(allValue + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
