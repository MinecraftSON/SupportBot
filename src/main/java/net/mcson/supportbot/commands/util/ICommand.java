package net.mcson.supportbot.commands.util;

import java.util.List;

public interface ICommand {

    String getName();

    /**
     * @apiNote 0 = default, 1 = helper, 2 = moderator, 3 = admin, 4 = owner
     * @return int
     */
    default int getPermissionLevel() {
        return 0;
    }

    String getHelp();

    default List<String> getAliases(){
        return List.of();
    }
}
