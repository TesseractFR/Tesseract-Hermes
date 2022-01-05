package onl.tesseract.hermes;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CommandManager {

    private final Collection<DiscordCommand> commands;

    CommandManager(final Collection<DiscordCommand> commands)
    {
        this.commands = commands;
    }
}
