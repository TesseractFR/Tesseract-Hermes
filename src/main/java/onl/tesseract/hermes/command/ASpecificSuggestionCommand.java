package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import onl.tesseract.hermes.DiscordRootCommand;
import onl.tesseract.hermes.DiscordSubCommand;
import onl.tesseract.hermes.HermesApplication;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.exception.SuggestionParsingException;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class ASpecificSuggestionCommand implements DiscordSubCommand {

    private final SuggestionCardParser suggestionCardParser;

    public ASpecificSuggestionCommand(final SuggestionCardParser suggestionCardParser)
    {
        this.suggestionCardParser = suggestionCardParser;
    }

    protected abstract Logger getLogger();

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
                                 getLogger().warn("Failed to parse given card " + card.getShortLink());
                                 return Optional.empty();
                             }
                         })
                         .ifPresentOrElse(suggestion -> {
                             process(event, suggestion);
                         }, () -> event.getHook()
                                       .sendMessage("Suggestion non trouvée")
                                       .delay(10, TimeUnit.SECONDS)
                                       .flatMap(Message::delete)
                                       .queue());
    }

    protected abstract void process(final SlashCommandEvent event, final Suggestion suggestion);

    @Override
    public Class<? extends DiscordRootCommand> getParentCommand()
    {
        return SuggestionCommand.class;
    }
}
