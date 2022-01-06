package onl.tesseract.hermes.command;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SuggestionCommand implements DiscordCommand {

    private final Board board;
    private final TList newSuggestionsList;

    public SuggestionCommand(final Board board)
    {
        this.board = board;
        newSuggestionsList = board.fetchLists()
                                  .stream()
                                  .filter(list -> list.getName().equals("Suggestions"))
                                  .findAny()
                                  .orElseThrow();
    }

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

        Card created = createCard(title, description);

        event.getHook()
             .sendMessage(created.getShortUrl())
             .queue();
    }

    private Card createCard(final String title, final String description)
    {
        Card card = new Card();
        card.setName(title);
        card.setDesc(description);
        return newSuggestionsList.createCard(card);
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
