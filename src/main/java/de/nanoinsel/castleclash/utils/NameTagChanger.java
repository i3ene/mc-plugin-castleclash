package de.nanoinsel.castleclash.utils;

import com.mojang.authlib.GameProfile;
import de.nanoinsel.castleclash.teams.TeamAction;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.Arrays;

public class NameTagChanger {

    private static org.bukkit.scoreboard.Team team;
    private static Scoreboard scoreboard;

    public static void changePlayerName(Player player, String prefix, String suffix, TeamAction action) {
        if (player.getScoreboard() == null || prefix == null || suffix == null || action == null) {
            return;
        }

        scoreboard = player.getScoreboard();

        if (scoreboard.getTeam(player.getName()) == null) {
            scoreboard.registerNewTeam(player.getName());
        }

        team = scoreboard.getTeam(player.getName());
        team.setPrefix(Color(prefix));
        team.setSuffix(Color(suffix));
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

        switch (action) {
            case CREATE:
                team.addPlayer(player);
                break;
            case UPDATE:
                team.unregister();
                scoreboard.registerNewTeam(player.getName());
                team = scoreboard.getTeam(player.getName());
                team.setPrefix(Color(prefix));
                team.setSuffix(Color(suffix));
                team.setNameTagVisibility(NameTagVisibility.ALWAYS);
                team.addPlayer(player);
                break;
            case DESTROY:
                team.unregister();
                break;
        }
    }

    private static String Color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    @Deprecated
    public static void changePlayerName(Player p, String name) {
        System.out.println(Arrays.toString(name.toCharArray()));
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl == p) continue;
            //REMOVES THE PLAYER
            ((CraftPlayer)pl).getHandle().b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf("REMOVE_PLAYER"), ((CraftPlayer)p).getHandle()));
            //CHANGES THE PLAYER'S GAME PROFILE
            GameProfile gp = ((CraftPlayer)p).getProfile();
            try {
                Field nameField = GameProfile.class.getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(gp, ChatColor.translateAlternateColorCodes('&', name));
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new IllegalStateException(ex);
            }
            //ADDS THE PLAYER
            ((CraftPlayer)pl).getHandle().b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf("ADD_PLAYER"), ((CraftPlayer)p).getHandle()));
            ((CraftPlayer)pl).getHandle().b.a(new PacketPlayOutEntityDestroy(p.getEntityId()));
            ((CraftPlayer)pl).getHandle().b.a(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
        }
    }

}