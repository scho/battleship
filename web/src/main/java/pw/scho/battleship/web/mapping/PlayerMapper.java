package pw.scho.battleship.web.mapping;

import pw.scho.battleship.model.Player;

public class PlayerMapper {

    public static PlayerDto map(Player player) {
        PlayerDto dto = new PlayerDto();

        dto.setName(player.getName());
        dto.setGamesLost(player.getGamesLost());
        dto.setGamesWon(player.getGamesWon());

        return dto;
    }
}
