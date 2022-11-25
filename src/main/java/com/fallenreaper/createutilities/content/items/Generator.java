package com.fallenreaper.createutilities.content.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Generator {

    /**
     * Generates a Blockstate JSON for the specified block using all of it's properties.
     * @param path Starting path ie "E://Modding/forge".
     * @param modid The modid for this blocks mod, used mainly as an identifier.
     * @param fileName Pretty self explanatory.
     * @param block The block that is relevant to the blockstate.
     * @deprecated Use genVanillaBlockState or genForgeBlockState instead of this.
     */
    public static void createJson(String path, String modid, String fileName, Block block) {
        File json = new File(path + "/assets/" + modid + "/blockstates/" + fileName + ".json");
        File folders = new File(path + "/assets/" + modid + "/blockstates/");
        if (json.exists()) return;
        folders.mkdirs();
        try {
            json.createNewFile();
            if (!json.canWrite()) json.setWritable(true);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            JsonObject obj = new JsonObject();
            JsonObject element = new JsonObject();
            Collection<BlockState> collec = block.getStateDefinition().getPossibleStates();
            for (BlockState state : collec) {
                Collection<Property<?>> properties = state.getProperties();
                String[] names = new String[properties.size()], values = new String[properties.size()];
                for (int i = 0; i < names.length; i++) {
                    Property<?> prop = (Property<?>) properties.toArray()[i];
                    names[i] = prop.getName();
                    values[i] = state.getProperties().stream().toString();
                }
                StringBuilder tagBuilder = new StringBuilder();
                for (int i = 0; i < properties.size(); i++) {
                    if (i == properties.size() - 1) {
                        tagBuilder.append(names[i]).append("=").append(values[i]);
                    } else tagBuilder.append(names[i]).append("=").append(values[i]).append(",");
                }

                element.add(tagBuilder.toString(), new JsonObject());
            }
            obj.add("variants", element);
            String[] jsonText = gson.toJson(obj).split("[{]");
            for (int i = 0; i < jsonText.length; i++) {
                if (!jsonText[i].endsWith("}")) jsonText[i] = jsonText[i] += "{";
            }
            List<String[]> linesList = new ArrayList<String[]>();
            for (String line : jsonText) {
                String[] array = line.split("},");
                for (int i = 0; i < array.length; i++) {
                    if (i != array.length - 1) array[i] = array[i] += "},";
                }
                linesList.add(array);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(json));
            for (String[] lines : linesList) {
                for (String line : lines) {
                    if (line.startsWith("}")) writer.newLine();
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a Blockstate JSON for the specified block using all of it's properties.
     * @author Animefan8888
     * @param path Starting path ie "E://Modding/forge".
     * @param modid The modid for this blocks mod, used mainly as an identifier.
     * @param fileName Pretty self explanatory.
     * @param block The block that is relevant to the blockstate.
     */


}