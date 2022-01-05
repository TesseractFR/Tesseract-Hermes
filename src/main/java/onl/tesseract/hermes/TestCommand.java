package onl.tesseract.hermes;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class TestCommand implements DiscordCommand {
    @Override
    public String name()
    {
        return "test";
    }

    @Override
    public void execute(final SlashCommandEvent event)
    {

    }
}
