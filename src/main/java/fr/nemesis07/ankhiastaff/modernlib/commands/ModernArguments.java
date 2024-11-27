package fr.nemesis07.ankhiastaff.modernlib.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ModernArguments {
    private String label;
    private String[] args;

    public ModernArguments(String label, String[] args) {
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String[] getArgs(int index) {
        int count = this.args.length - index;
        if (count <= 0) {
            return new String[0];
        } else {
            String[] newArgs = new String[count];

            for(int newIndex = 0; index < count; ++newIndex) {
                newArgs[newIndex] = this.getText(index++);
            }

            return newArgs;
        }
    }

    public String getText(int index) {
        return !this.hasArg(index) ? null : this.args[index];
    }

    public int getNumber(int index) {
        String text = this.getText(index);
        if (text == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(this.args[index]);
            } catch (NumberFormatException var4) {
                return -1;
            }
        }
    }

    public Player getPlayer(int index) {
        String text = this.getText(index);
        return text == null ? null : Bukkit.getPlayer(text);
    }

    public boolean hasArg(int index) {
        return index < this.args.length;
    }

    public boolean hasArg() {
        return this.args.length > 0;
    }
}