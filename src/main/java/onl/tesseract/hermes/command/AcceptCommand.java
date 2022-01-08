package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.suggestion.SuggestionApprovalType;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AcceptCommand extends ASpecificSuggestionCommand {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCommand.class);

    public AcceptCommand(final SuggestionCardParser suggestionCardParser)
    {
        super(suggestionCardParser);
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
    protected Logger getLogger()
    {
        return logger;
    }

    @Override
    protected void process(final SlashCommandEvent event, final Suggestion suggestion)
    {
        String newStatusString = Objects.requireNonNull(event.getOption("état")).getAsString();
        OptionMapping messageOption = event.getOption("message");

        SuggestionApprovalType approvalType = SuggestionApprovalType.valueOf(newStatusString);
        String message = messageOption == null ? null : messageOption.getAsString();

        suggestion.accept(approvalType, message);
        logger.info("Accepted suggestion {}. Moved to {}.", suggestion.getTrelloCard().getShortLink(), approvalType.getTrelloList().getName());
        event.getHook().sendMessage(":thumbsup:")
             .queue();
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
