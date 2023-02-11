package core;

import game.Player;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Data {
    private static final String settingsPath = System.getProperty("user.dir") + "\\res\\data\\settings.json";
    private static final String playerPath = System.getProperty("user.dir") + "\\res\\data\\player.json";
    public static HashMap<String, Boolean> settings;

    private static FileWriter wFile;
    private static FileReader rFile;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JSONParser parser = new JSONParser();

    public static Player load() {
        try {
            rFile = new FileReader(settingsPath);

            JSONObject obj = (JSONObject) parser.parse(rFile);
            settings = mapper.readValue(obj.toJSONString(), new TypeReference<>() {});

            settings.putIfAbsent("music_enabled", true);
            settings.putIfAbsent("sound_enabled", true);

        } catch (IOException | ParseException e) {
            e.printStackTrace();

        } finally {
            try {
                rFile.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        try {
            rFile = new FileReader(playerPath);
            JSONObject obj = (JSONObject) parser.parse(rFile);

            System.out.print("\nload_0");
            return mapper.readValue(obj.toJSONString(), Player.class);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.print("\nload_1");
            return new Player();

        } finally {
            try {
                rFile.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public static void save(Player plr) {
        try {
            wFile = new FileWriter(settingsPath);
            wFile.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(settings));

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                wFile.flush();
                wFile.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        try {
            wFile = new FileWriter(playerPath);
            wFile.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(plr));

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                wFile.flush();
                wFile.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        System.out.print("\nsave_0");
    }
}
