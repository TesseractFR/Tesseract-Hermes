package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import onl.tesseract.hermes.HermesApplication;
import onl.tesseract.hermes.exception.SuggestionParsingException;
import onl.tesseract.hermes.suggestion.SuggestionApprovalType;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class AcceptCommand implements DiscordSubCommand {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCommand.class);

    private final SuggestionCardParser suggestionCardParser;

    public AcceptCommand(final SuggestionCardParser suggestionCardParser)
    {
        this.suggestionCardParser = suggestionCardParser;
    }

    @Override
    public String name()
    {
        return "accepter";
    }

    @Override
    public String description()
    {
        return "Accepter une suggestion";
    }

    @Override
    public void execute(final SlashCommandEvent event)
    {
        event.deferReply().queue();

        String suggestionId = Objects.requireNonNull(event.getOption("suggestion")).getAsString();
        String newStatusString = Objects.requireNonNull(event.getOption("état")).getAsString();
        OptionMapping messageOption = event.getOption("message");

        SuggestionApprovalType approvalType = SuggestionApprovalType.valueOf(newStatusString);
        String message = messageOption == null ? null : messageOption.getAsString();
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
                             suggestion.accept(approvalType, message);
                             logger.info("Accepted suggestion {}. Moved to {}.", suggestion.getTrelloCard().getShortLink(), approvalType.getTrelloList().getName());
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
                new OptionData(OptionType.STRING, "état", "Nouvel état de la suggestion", true)
                        .addChoice("pour plus tard", SuggestionApprovalType.FOR_LATER.toString())
                        .addChoice("en discussion", SuggestionApprovalType.IN_DISCUSSION.toString())
                        .addChoice("discutée", SuggestionApprovalType.DISCUSSED.toString()),
                new OptionData(OptionType.STRING, "message", "Message supplémentaire", false)
        );
    }

    @Override
    public Class<? extends DiscordRootCommand> getParentCommand()
    {
        return SuggestionCommand.class;
    }
}
