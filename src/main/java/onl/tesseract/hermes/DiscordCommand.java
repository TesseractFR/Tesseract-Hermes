package onl.tesseract.hermes;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface DiscordCommand {

    String name();

    void execute(final SlashCommandEvent event);
}
