package me.caseload.knockbacksync.stats;

import me.caseload.knockbacksync.KnockbackSync;
import org.bstats.bukkit.Metrics;

public class StatsManager {

    public static Metrics metrics;

    public static void init() {
        KnockbackSync plugin = KnockbackSync.getInstance();

        // Use Folia's GlobalRegionScheduler
        plugin.getServer().getGlobalRegionScheduler().execute(plugin, () -> {
            initializeMetrics();
        });
    }

    private static void initializeMetrics() {
        try {
            BuildTypePie.determineBuildType(); // Function to calculate hash
            metrics = new Metrics(KnockbackSync.getInstance(), 23568);
            metrics.addCustomChart(new PlayerVersionsPie());
            metrics.addCustomChart(new BuildTypePie());
        } catch (Exception e) {
            KnockbackSync.getInstance().getLogger().warning("Failed to initialize bStats metrics: " + e.getMessage());
        }
    }
}
