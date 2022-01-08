package onl.tesseract.hermes.command;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.exception.SuggestionParsingException;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import onl.tesseract.hermes.suggestion.TrelloList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ListCommand implements DiscordSubCommand {

    private static final Logger logger = LoggerFactory.getLogger(ListCommand.class);

    final Board board;
    private final SuggestionCardParser cardParser;

    public ListCommand(final Board board, final SuggestionCardParser cardParser)
    {
        this.board = board;
        this.cardParser = cardParser;
    }

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
        event.deferReply().queue();
        TList list = TrelloList.SUGGESTIONS.getList();
        Collection<Card> cards = board.fetchCards()
                                      .stream()
                                      .filter(card -> card.getIdList().equals(list.getId()))
                                      .collect(Collectors.toList());

        List<String> lines = cards.stream()
                                  .map(card -> {
                                      try
                                      {
                                          return cardParser.parse(card);
                                      }
                                      catch (SuggestionParsingException e)
                                      {
                                          logger.debug("Failed to parse card : " + e.getMessage());
                                          return null;
                                      }
                                  })
                                  .filter(Objects::nonNull)
                                  .map(this::getSuggestionShortInfo)
                                  .collect(Collectors.toList());
        if (lines.isEmpty())
        {
            event.getHook().sendMessage("Aucune suggestion en attente")
                 .queue();
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder()
                    .append("Suggestions en attente :");
            lines.forEach(stringBuilder::append);
            event.getHook()
                 .sendMessage(stringBuilder.toString())
                 .queue();
        }
    }

    private String getSuggestionShortInfo(final Suggestion suggestion)
    {
        return "\nTitre : " + suggestion.getTitle()
                + " | Auteur : " + suggestion.getDiscordMember().getUser().getAsTag()
                + " | " + suggestion.getTrelloCard().getShortUrl();
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
