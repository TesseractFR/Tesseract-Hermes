package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SuggestionCommand implements DiscordCommand {
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
        event.deferReply().queue();
        String title = Objects.requireNonNull(event.getOption("titre")).getAsString();
        String description = Objects.requireNonNull(event.getOption("description")).getAsString();
        event.getHook()
             .sendMessage("test")
             .queue();
    }

    @Override
    public List<OptionData> options()
    {
        return List.of(
                new OptionData(OptionType.STRING, "titre", "Titre de la suggestion. Veuillez mettre un titre clair et précis", true),
                new OptionData(OptionType.STRING, "description", "Description de la suggestion.", true)
        );
    }
}
