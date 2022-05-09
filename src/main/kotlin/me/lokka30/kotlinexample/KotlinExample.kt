package me.lokka30.kotlinexample

import me.lokka30.kotlinexample.commands.ExampleCommand
import me.lokka30.kotlinexample.listeners.ExampleListener
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.EnumSet

class KotlinExample : JavaPlugin() {

    companion object {
        var instance: KotlinExample? = null
        private set
    }

    override fun onEnable() {
        val start = System.currentTimeMillis()

        instance = this

        logger.info("Registering listeners...")
        server.pluginManager.registerEvents(ExampleListener, this)

        logger.info("Registering commands...")
        getCommand("example")?.setExecutor(ExampleCommand)

        logger.info("Printing the rainbow...")
        printRainbow()

        val duration = System.currentTimeMillis() - start
        logger.info("Plugin enabled (took ${duration}ms)")
    }

    private fun printRainbow() {
        val rainbowBuilder = StringBuilder()
        EnumSet.of(
            ChatColor.RED,
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.DARK_PURPLE,
            ChatColor.LIGHT_PURPLE
        ).forEach {
            rainbowBuilder.append("$it###")
        }
        logger.info(rainbowBuilder.toString())
    }
}