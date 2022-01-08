package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
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
import java.util.concurrent.TimeUnit;

@Component
public class CommentCommand extends ASpecificSuggestionCommand {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCommand.class);

    public CommentCommand(final SuggestionCardParser suggestionCardParser)
    {
        super(suggestionCardParser);
    }

    @Override
    public String name()
    {
        return "comment";
    }

    @Override
    public String description()
    {
        return "Ajouter un commentaire à une suggestion";
    }

    @Override
    protected void process(final SlashCommandEvent event, final Suggestion suggestion)
    {
        final String commentContent = Objects.requireNonNull(event.getOption("commentaire")).getAsString();

        final String commentFullText = String.format("%s > %s", event.getMember().getUser().getAsTag(), commentContent);
        suggestion.getTrelloCard().addComment(commentFullText);
        suggestion.getTrelloCard().update();

        Message originalMessage = suggestion.getResponseMessage();
        if (originalMessage == null)
            throw new IllegalStateException("Suggestion has no reply");
        originalMessage.reply(new MessageBuilder()
                               .append(event.getMember())
                               .append(" > ")
                               .append(commentContent)
                               .build())
                       .queue();

        event.getHook()
             .sendMessage(":thumbsup:")
             .delay(5, TimeUnit.SECONDS)
             .flatMap(Message::delete)
             .queue();

        logger.info("Added a comment to card {}: {} ", suggestion.getTrelloCard().getShortLink(), commentFullText);
    }

    @Override
    public List<OptionData> options()
    {
        return List.of(
                new OptionData(OptionType.STRING, "suggestion", "Id de la suggestion", true),
                new OptionData(OptionType.STRING, "commentaire", "Commentaire", true)
        );
    }

    @Override
    protected Logger getLogger()
    {
        return logger;
    }
}
