package onl.tesseract.hermes.command;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.TList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import onl.tesseract.hermes.DiscordCommand;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.SuggestionBuilder;
import org.springframework.stereotype.Component;

import java.awt.*;
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

        Suggestion suggestion = new SuggestionBuilder().setTitle(title)
                                                       .setDescription(description)
                                                       .setDiscordMember(event.getMember())
                                                       .build();

        suggestion.submit();
        MessageEmbed reply = computeEmbedMessage(suggestion);

        Message sentReply = event.getHook()
                                .sendMessageEmbeds(reply)
                                .complete();

        suggestion.setResponseMessage(sentReply);
    }

    private MessageEmbed computeEmbedMessage(final Suggestion suggestion)
    {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(suggestion.getTitle())
                    .setColor(new Color(52, 152, 219))
                    .setFooter("Proposé par " + suggestion.getDiscordMember().getUser().getAsTag())
                    .addField("État", "En attente", false)
                    .addField("Trello id", suggestion.getTrelloCard().getShortLink(), false)
                    .addField("Trello url", suggestion.getTrelloCard().getShortUrl(), false)
                    .setDescription(suggestion.getDescription());

        return embedBuilder.build();
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
