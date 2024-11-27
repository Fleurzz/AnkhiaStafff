package fr.nemesis07.ankhiastaff.modernlib.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ModernCommand extends Command {
    public ModernCommand(String name) {
        super(name);
    }

    public ModernCommand(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));
    }

    public void register() {
        this.unregisterBukkitCommand();

        try {
            Object commandMap = this.getCommandMap();
            Method registerMethod = this.getRegisterMethod(commandMap);
            registerMethod.invoke(commandMap, this.getName(), this);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException var3) {
            AnkhiaStaff.getInstance().getLogger().severe("Exception while handling command register");
            var3.printStackTrace();
        }

    }

    private Method getRegisterMethod(Object commandMap) throws NoSuchMethodException, SecurityException {
        return commandMap.getClass().getMethod("register", String.class, Command.class);
    }

    private Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        boolean accessible = objectField.isAccessible();
        if (!accessible) {
            objectField.setAccessible(true);
        }

        Object result = objectField.get(object);
        if (!accessible) {
            objectField.setAccessible(false);
        }

        return result;
    }

    private Object getCommandMap() throws IllegalAccessException, NoSuchFieldException {
        Server server = Bukkit.getServer();
        return this.getPrivateField(server, "commandMap");
    }

    public void unregisterBukkitCommand() {
        try {
            Object commandMap = this.getCommandMap();
            Object map = null;

            try {
                map = this.getPrivateField(commandMap, "knownCommands");
            } catch (NoSuchFieldException var6) {
                Method getKnownCommandsMethod = commandMap.getClass().getMethod("getKnownCommands");
                map = getKnownCommandsMethod.invoke(commandMap);
            }

            Map<String, Command> knownCommands = (HashMap)map;
            knownCommands.remove(this.getName());
            Iterator var8 = this.getAliases().iterator();

            while(var8.hasNext()) {
                String alias = (String)var8.next();
                if (knownCommands.containsKey(alias) && ((Command)knownCommands.get(alias)).toString().contains(this.getName())) {
                    knownCommands.remove(alias);
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        this.onCommand(sender, new ModernArguments(label, args));
        return true;
    }

    public abstract void onCommand(CommandSender var1, ModernArguments var2);
}