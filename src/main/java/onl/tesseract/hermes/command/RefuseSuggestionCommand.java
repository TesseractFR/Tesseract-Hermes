package onl.tesseract.hermes.command;

import com.julienvey.trello.domain.Board;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import onl.tesseract.hermes.HermesApplication;
import onl.tesseract.hermes.exception.SuggestionParsingException;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import onl.tesseract.hermes.suggestion.SuggestionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RefuseSuggestionCommand implements DiscordSubCommand {

    private static final Logger logger = LoggerFactory.getLogger(RefuseSuggestionCommand.class);

    private final Board board;
    private final SuggestionCardParser suggestionCardParser;

    public RefuseSuggestionCommand(final Board board, final SuggestionCardParser suggestionCardParser)
    {
        this.board = board;
        this.suggestionCardParser = suggestionCardParser;
    }

    @Override
    public String name()
    {
        return "refuser";
    }

    @Override
    public String description()
    {
        return "Refuser une suggestion";
    }

    @Override
    public void execute(final SlashCommandEvent event)
    {
        event.deferReply().queue();
        String suggestionId = Objects.requireNonNull(event.getOption("suggestion")).getAsString();
        HermesApplication.findCardByShortLink(suggestionId)
                         .flatMap(card -> {
                             try
                             {
                                 return Optional.of(suggestionCardParser.parse(card));
                             }
                             catch (SuggestionParsingException e)
                             {
                                 logger.warn("Failed to parse given card " + card.getShortLink());
                                 return Optional.empty();
                             }
                         })
                         .ifPresentOrElse(suggestion -> {
                             suggestion.setStatus(SuggestionStatus.REFUSED);
                             suggestion.updateDiscordMessage();
                             event.getHook().sendMessage(":thumbsup:")
                                  .queue();
                         }, () -> event.getHook()
                                       .sendMessage("Suggestion non trouvée")
                                       .delay(10, TimeUnit.SECONDS)
                                       .flatMap(Message::delete)
                                       .queue());

    }

    @Override
    public List<OptionData> options()
    {
        return List.of(
                new OptionData(OptionType.STRING, "suggestion", "Id de la suggestion", true),
                new OptionData(OptionType.STRING, "raison", "Raison du refus", true)
        );
    }

    @Override
    public Class<? extends DiscordRootCommand> getParentCommand()
    {
        return SuggestionCommand.class;
    }
}
