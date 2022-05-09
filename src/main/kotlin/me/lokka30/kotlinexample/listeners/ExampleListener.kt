package me.lokka30.kotlinexample.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor.AQUA
import org.bukkit.ChatColor.BLUE
import org.bukkit.ChatColor.DARK_GRAY
import org.bukkit.ChatColor.GRAY
import org.bukkit.ChatColor.GREEN
import org.bukkit.ChatColor.ITALIC
import org.bukkit.ChatColor.LIGHT_PURPLE
import org.bukkit.ChatColor.RED
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

object ExampleListener : Listener {

    private val positionBarMap = HashMap<UUID, BossBar>()
    private val worldNames = mapOf(
        "world" to "${GREEN}${ITALIC}The Overworld",
        "world_nether" to "$RED${ITALIC}The Nether",
        "world_the_end" to "$LIGHT_PURPLE${ITALIC}The End"
    )

    @EventHandler
    public fun onJoin(event: PlayerJoinEvent) {
        if (event.player.gameMode == GameMode.SURVIVAL) {
            event.player.sendMessage(
                "${AQUA}Hello, ${event.player.name}! You are in Survival mode!"
            )
        } else {
            event.player.sendMessage(
                "${LIGHT_PURPLE}Hello, ${event.player.name}! You are not in Survival mode!"
            )
        }

        val bossBar = Bukkit.createBossBar(
            getFormattedLocation(event.player.location),
            BarColor.WHITE,
            BarStyle.SOLID
        )
        positionBarMap[event.player.uniqueId] = bossBar
        bossBar.addPlayer(event.player)
    }

    @EventHandler
    public fun onQuit(event: PlayerQuitEvent) {
        positionBarMap[event.player.uniqueId]?.removeAll()
        positionBarMap.remove(event.player.uniqueId)
    }

    @EventHandler
    public fun onMove(event: PlayerMoveEvent) {
        event.to?.let {
            if (!hasMovedBlockXYZ(event.from, it)) return
            updatePositionBar(event.player, it)
        }
    }

    @EventHandler
    public fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        updatePositionBar(event.player)
    }

    private fun updatePositionBar(player: Player, location: Location = player.location) {
        positionBarMap[player.uniqueId]?.setTitle(getFormattedLocation(location))
    }

    private fun hasMovedBlockXYZ(from: Location, to: Location): Boolean {
        return (from.blockX != to.blockX) or (from.blockY != to.blockY) or (from.blockZ != to.blockZ)
    }

    private fun getFormattedLocation(loc: Location): String {
        return if (loc.world == null) {
            "$DARK_GRAY($RED{Err: Unknown Location}$DARK_GRAY)"
        } else {
            "$DARK_GRAY($AQUA${loc.blockX}$GRAY, " +
                    "${AQUA}${loc.blockY}${GRAY}, " +
                    "${AQUA}${loc.blockZ}${GRAY} " +
                    "in ${getFormattedWorldName(loc.world)}${DARK_GRAY})"
        }
    }

    private fun getFormattedWorldName(world: World?): String {
        if (world == null) {
            return "${RED}{Err: Unknown World}"
        }

        val worldName = world.name

        return if (worldName in worldNames) {
            worldNames[worldName]!!
        } else {
            "${BLUE}$ITALIC${worldName}"
        }
    }
}