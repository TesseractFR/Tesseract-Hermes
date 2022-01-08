package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RefuseSuggestionCommand extends ARestrictedSuggestionCommand {

    private static final Logger logger = LoggerFactory.getLogger(RefuseSuggestionCommand.class);

    public RefuseSuggestionCommand(final SuggestionCardParser suggestionCardParser)
    {
        super(suggestionCardParser);
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
    protected Logger getLogger()
    {
        return logger;
    }

    @Override
    protected void process(final SlashCommandEvent event, final Suggestion suggestion)
    {
        String refusalReason = Objects.requireNonNull(event.getOption("raison")).getAsString();
        refuse(suggestion, refusalReason);

        suggestion.getResponseMessage()
                  .reply(new MessageBuilder()
                          .append(suggestion.getDiscordMember())
                          .append(", ta suggestion \"")
                          .append(suggestion.getTitle())
                          .append("\" a été refusée.")
                          .build())
                  .queue();

        event.getHook().sendMessage(":thumbsup:")
             .queue();
    }

    private void refuse(final Suggestion suggestion, final String reason)
    {
        suggestion.refuse(reason);
        logger.info("Suggestion {} refused. Trello card closed.", suggestion.getTrelloCard().getShortLink());
    }

    @Override
    public List<OptionData> options()
    {
        return List.of(
                new OptionData(OptionType.STRING, "suggestion", "Id de la suggestion", true),
                new OptionData(OptionType.STRING, "raison", "Raison du refus", true)
        );
    }
}
