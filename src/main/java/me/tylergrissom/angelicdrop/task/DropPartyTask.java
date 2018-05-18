package me.tylergrissom.angelicdrop.task;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropController;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright Tyler Grissom 2018
 */
@AllArgsConstructor
public class DropPartyTask extends BukkitRunnable {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private Selection selection;

    private Location getRandomLocation(Location min, Location max) {
        Preconditions.checkArgument(min.getWorld() == max.getWorld());
        
        double minX = Math.min(min.getX(), max.getX());
        double minY = Math.min(min.getY(), max.getY());
        double minZ = Math.min(min.getZ(), max.getZ());

        double maxX = Math.max(min.getX(), max.getX());
        double maxY = Math.max(min.getY(), max.getY());
        double maxZ = Math.max(min.getZ(), max.getZ());

        return new Location(min.getWorld(), randomDouble(minX, maxX), randomDouble(minY, maxY), randomDouble(minZ, maxZ));
    }

    private double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }

    @Override
    public void run() {
        if (getSelection().getWorld() == null) {
            return;
        }

        getSelection().getWorld().dropItem(getRandomLocation(getSelection().getMinimumPoint(), getSelection().getMaximumPoint()), getPlugin().getController().getRandomItem());
    }
}
