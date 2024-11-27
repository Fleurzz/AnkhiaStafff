package fr.nemesis07.ankhiastaff.modernlib.config;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.utils.ChatColors;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigWrapper {
    private ConfigurationSection config = null;
    private String path = null;
    private String fileName = null;
    private Map<String, String> colorTextMap = new HashMap();

    public ConfigWrapper(ConfigurationSection config) {
        this.config = config;
    }

    public ConfigWrapper(String fileName) {
        this.setFile(fileName);
    }

    public ConfigWrapper() {
    }

    public void setFile(String fileName) {
        if (fileName != null) {
            this.path = (new File(AnkhiaStaff.getInstance().getDataFolder(), fileName)).getPath();
        } else {
            this.path = null;
        }

        this.fileName = fileName;
    }

    public ConfigWrapper saveDefault() {
        if (this.path != null) {
            File configFile = new File(this.path);
            if (!configFile.exists()) {
                try {
                    InputStream inputStream = AnkhiaStaff.getInstance().getResource(this.fileName);

                    try {
                        createParentFolder(configFile);
                        if (inputStream != null) {
                            Files.copy(inputStream, configFile.toPath(), new CopyOption[0]);
                        } else {
                            configFile.createNewFile();
                        }
                    } catch (Throwable var6) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable var5) {
                                var6.addSuppressed(var5);
                            }
                        }

                        throw var6;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException var7) {
                    var7.printStackTrace();
                }
            }
        }

        return this;
    }

    public ConfigWrapper load(String fileName) {
        this.setFile(fileName);
        return this.load();
    }

    public ConfigWrapper load() {
        if (this.path != null) {
            try {
                this.colorTextMap.clear();
                this.config = YamlConfiguration.loadConfiguration(new File(this.path));
            } catch (IllegalArgumentException var2) {
                var2.printStackTrace();
            }
        }

        return this;
    }

    public void save() {
        if (this.path != null) {
            if (this.config instanceof YamlConfiguration) {
                try {
                    ((YamlConfiguration)this.config).save(this.path);
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }

        }
    }

    public ConfigurationSection getConfig() {
        return this.config;
    }

    public boolean isLoaded() {
        return this.config != null;
    }

    public List<String> getStringList(String key) {
        return !this.isLoaded() ? Collections.emptyList() : this.config.getStringList(key);
    }

    public List<String> getTextList(String key) {
        List<String> textList = new ArrayList();
        List<String> stringList = this.getStringList(key);
        Iterator var4 = stringList.iterator();

        while(var4.hasNext()) {
            String text = (String)var4.next();
            textList.add(ChatColors.color(text));
        }

        return textList;
    }

    public List<String> getTextList(String key, String... placeholders) {
        List<String> textList = new ArrayList();
        List<String> stringList = this.getStringList(key);
        Iterator var5 = stringList.iterator();

        while(var5.hasNext()) {
            String text = (String)var5.next();
            String processedText = this.replacePlaceholders(text, placeholders);
            textList.add(ChatColors.color(processedText));
        }

        return textList;
    }

    public String getText(String key) {
        if (this.colorTextMap.containsKey(key)) {
            return (String)this.colorTextMap.get(key);
        } else {
            String text = ChatColors.color(this.getString(key));
            this.colorTextMap.put(key, text);
            return text;
        }
    }

    public String getText(String key, String... placeholders) {
        String text = this.getText(key);

        for(int i = 0; i < placeholders.length; i += 2) {
            text = this.replacePlaceholders(text, placeholders);
        }

        return text;
    }

    private String replacePlaceholders(String text, String... placeholders) {
        for(int i = 0; i < placeholders.length; i += 2) {
            String placeholder = placeholders[i];
            String replacement = i + 1 < placeholders.length ? placeholders[i + 1] : "";
            text = text.replace(placeholder, replacement);
        }

        return text;
    }

    public String getString(String key) {
        return !this.isLoaded() ? "undefined" : this.config.getString(key, "");
    }

    public int getInt(String key, int def) {
        return !this.isLoaded() ? def : this.config.getInt(key);
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return !this.isLoaded() ? false : this.config.getBoolean(key);
    }

    public ConfigWrapper getSection(String key) {
        if (!this.isLoaded()) {
            return null;
        } else {
            ConfigurationSection section = this.config.getConfigurationSection(key);
            return section == null ? null : new ConfigWrapper(section);
        }
    }

    public Set<String> getKeys() {
        return !this.isLoaded() ? Collections.emptySet() : this.config.getKeys(false);
    }

    private static void createParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

    }

    public boolean isConfigurationSection(String path) {
        return this.config.isConfigurationSection(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public long getLong(String path, long def) {
        return this.config.getLong(path, def);
    }
}