package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuggestionCommand implements DiscordRootCommand {
    @Override
    public String name()
    {
        return "suggestion";
    }

    @Override
    public String description()
    {
        return "Faire une suggestion";
    }

    @Override
    public void execute(final SlashCommandEvent event)
    {

    }

    @Override
    public List<OptionData> options()
    {
        return List.of();
    }
}
