package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.DraftAPI;
import fr.communaywen.core.utils.LinkerAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class OnPlayers implements Listener {

    private LuckPerms luckPerms;
    private LinkerAPI linkerAPI;

    public void setLuckPerms(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void setLinkerAPI(LinkerAPI linkerAPI) {
        this.linkerAPI = linkerAPI;
    }

    public void addPermission(User user, String permission) {
        // Add the permission
        user.data().add(Node.builder(permission).build());

        // Now we need to save changes.
        luckPerms.getUserManager().saveUser(user);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException { // Donne une permissions en fonction du niveau
        Player player = event.getPlayer();
        DraftAPI draftAPI = new DraftAPI();

        JSONObject data = new JSONObject(draftAPI.getTop());
        JSONArray users = data.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            String discordId = user.getString("id");

            if (this.linkerAPI.getUserId(player).equals(discordId)){
                User lpPlayer = this.luckPerms.getPlayerAdapter(Player.class).getUser(player);

                int level = user.getInt("level");
                if (level < 10){ break; }

                addPermission(lpPlayer, "ayw.levels.10");
                if (level >= 20){
                    addPermission(lpPlayer, "ayw.levels.20");
                }
                if (level >= 30){
                    addPermission(lpPlayer, "ayw.levels.30");
                }
                if (level >= 40){
                    addPermission(lpPlayer, "ayw.levels.40");
                }
                if (level >= 50) {
                    addPermission(lpPlayer, "ayw.levels.50");
                }
            }
        }
    }
}