package io.stealingdapenta.mc2048.utils;

import io.stealingdapenta.mc2048.MC2048;
import org.bukkit.Bukkit;

public class RepeatingUpdateTask implements Runnable {

    private final int taskID;

    public RepeatingUpdateTask(long delay, long period) {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MC2048.getInstance(), this, delay, period);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @Override
    public void run() {

    }
}
