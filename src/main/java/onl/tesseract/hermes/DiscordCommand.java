package onl.tesseract.hermes;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface DiscordCommand {

    String name();

    String description();

    void execute(final SlashCommandEvent event);

    List<OptionData> options();
}
