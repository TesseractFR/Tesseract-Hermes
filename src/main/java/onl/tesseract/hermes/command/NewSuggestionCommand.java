package onl.tesseract.hermes.command;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.TList;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.SuggestionBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class NewSuggestionCommand implements DiscordSubCommand {

    private final Board board;
    private final TList newSuggestionsList;

    public NewSuggestionCommand(final Board board)
    {
        this.board = board;
        newSuggestionsList = board.fetchLists()
                                  .stream()
                                  .filter(list -> list.getName().equals("Suggestions"))
                                  .findAny()
                                  .orElseThrow();
    }

    @Override
    public Class<? extends DiscordRootCommand> getParentCommand()
    {
        return SuggestionCommand.class;
    }

    @Override
    public String name()
    {
        return "nouveau";
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

        Suggestion suggestion = new SuggestionBuilder().setTitle(title)
                                                       .setDescription(description)
                                                       .setDiscordMember(event.getMember())
                                                       .build();

        suggestion.submit();
        MessageEmbed reply = SuggestionUtils.computeEmbedMessage(suggestion);

        Message sentReply = event.getHook()
                                .sendMessageEmbeds(reply)
                                .complete();

        suggestion.setResponseMessage(sentReply);
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
