package me.caseload.knockbacksync.manager;

import lombok.Getter;
import lombok.Setter;
import me.caseload.knockbacksync.KnockbackSync;
import me.caseload.knockbacksync.runnable.PingRunnable;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

@Getter
@Setter
public class ConfigManager {

    private boolean toggled;
    private boolean runnableEnabled;
    private boolean updateAvailable;
    private boolean notifyUpdate;

    private long runnableInterval;
    private long combatTimer;
    private long spikeThreshold;

    private String enableMessage;
    private String disableMessage;
    private String playerEnableMessage;
    private String playerDisableMessage;
    private String playerIneligibleMessage;
    private String reloadMessage;

    private ScheduledTask pingTask;

    public void loadConfig(boolean reloadConfig) {
        KnockbackSync instance = KnockbackSync.getInstance();

        if (reloadConfig)
            instance.reloadConfig();

        toggled = instance.getConfig().getBoolean("enabled", true);

        boolean newRunnableEnabled = instance.getConfig().getBoolean("runnable.enabled", true);
        if (runnableEnabled && !newRunnableEnabled && pingTask != null)
            pingTask.cancel();

        runnableEnabled = newRunnableEnabled;

        notifyUpdate = instance.getConfig().getBoolean("notify_updates", true);
        runnableInterval = instance.getConfig().getLong("runnable.interval", 5L);
        combatTimer = instance.getConfig().getLong("runnable.timer", 30L);
        spikeThreshold = instance.getConfig().getLong("spike_threshold", 20L);
        enableMessage = instance.getConfig().getString("enable_message", "&aSuccessfully enabled KnockbackSync.");
        disableMessage = instance.getConfig().getString("disable_message", "&cSuccessfully disabled KnockbackSync.");
        playerEnableMessage = instance.getConfig().getString("player_enable_message", "&aSuccessfully enabled KnockbackSync for %player%.");
        playerDisableMessage = instance.getConfig().getString("player_disable_message", "&cSuccessfully disabled KnockbackSync for %player%.");
        playerIneligibleMessage = instance.getConfig().getString("player_ineligible_message", "&c%player% is ineligible for KnockbackSync. If you believe this is an error, please open an issue on the github page.");
        reloadMessage = instance.getConfig().getString("reload_message", "&aSuccessfully reloaded KnockbackSync.");

        if (runnableEnabled) {
            if (pingTask != null) {
                pingTask.cancel();
            }
            // Use a minimum of 1 tick for the initial delay and interval
            long delay = Math.max(1, runnableInterval);
            pingTask = instance.getServer().getGlobalRegionScheduler().runAtFixedRate(instance, task -> {
                new PingRunnable().run();
            }, delay, delay);
        }
    }
}