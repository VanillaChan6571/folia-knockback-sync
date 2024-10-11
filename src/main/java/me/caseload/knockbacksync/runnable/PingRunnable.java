package me.caseload.knockbacksync.runnable;

import me.caseload.knockbacksync.KnockbackSync;
import me.caseload.knockbacksync.manager.CombatManager;
import me.caseload.knockbacksync.manager.PlayerData;
import me.caseload.knockbacksync.manager.PlayerDataManager;

import java.util.UUID;

public class PingRunnable implements Runnable {

    @Override
    public void run() {
        if (!KnockbackSync.getInstance().getConfigManager().isToggled())
            return;

        for (UUID uuid : CombatManager.getPlayers()) {
            PlayerData playerData = PlayerDataManager.getPlayerData(uuid);
            playerData.sendPing();
        }
    }
}
