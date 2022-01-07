package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListCommand implements DiscordSubCommand {
    @Override
    public String name()
    {
        return "list";
    }

    @Override
    public String description()
    {
        return "Lister les suggestions en attente";
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

    @Override
    public Class<? extends DiscordRootCommand> getParentCommand()
    {
        return SuggestionCommand.class;
    }
}
