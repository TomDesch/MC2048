package io.stealingdapenta.mc2048.utils.data;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class RepeatingUpdateTask implements Runnable {

    private final int taskID;

    public RepeatingUpdateTask(Plugin plugin, long delay, long period) {
        this.taskID = Bukkit.getScheduler()
                            .scheduleSyncRepeatingTask(plugin, this, delay, period);
    }

    public void cancel() {
        Bukkit.getScheduler()
              .cancelTask(taskID);
    }

    @Override
    public void run() {
    }
}
