package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;

public class CmdTP extends PlotCommand {

    public CmdTP(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_TP)) {
            if (manager.isPlotWorld(player) || serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                if (args.length == 2 || args.length == 3) {
                    IWorld world;
                    if (args.length == 3) {

                        world = serverBridge.getWorld(args[2]);

                        if (world == null) {
                            player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                            return true;
                        }
                    } else if (manager.isPlotWorld(player)) {
                        world = player.getWorld();
                    } else {
                        world = manager.getFirstWorld();
                    }

                    if (PlotId.isValidID(args[1])) {
                        PlotId id2 = new PlotId(args[1]);
                        if (!manager.isPlotWorld(world)) {
                            player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                        } else {
                            ILocation location = manager.getPlotHome(world, id2);
                            Plot plot = manager.getPlotById(id2, world);
                            InternalPlotTeleportEvent
                                    event =
                                    serverBridge.getEventFactory().callPlotTeleportEvent(world, plot, player, location, id2);

                            if (!event.isCancelled()) {
                                player.setLocation(location);
                            }
                        }
                    }
                } else if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(
                            C("WordUsage") + ": §c/plotme tp <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                } else {
                    player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
