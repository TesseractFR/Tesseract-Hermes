package onl.tesseract.hermes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandManager extends ListenerAdapter {

    private final Collection<DiscordCommand> commands;
    private final Guild guild;

    CommandManager(final Collection<DiscordCommand> commands, final Guild guild)
    {
        this.commands = commands;
        this.guild = guild;
    }

    @Override
    public void onSlashCommand(@NotNull final SlashCommandEvent event)
    {
        commands.stream()
                .filter(command -> command.name().equals(event.getName()))
                .findAny()
                .ifPresent(command -> command.execute(event));
    }

    public void registerCommands()
    {
        List<CommandData> datas = commands.stream()
                                          .map(command -> new CommandData(command.name(), command.description())
                                                  .addOptions(command.options()))
                                          .collect(Collectors.toList());
        guild.updateCommands()
             .addCommands(datas)
             .queue();
    }
}
