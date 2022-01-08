package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import onl.tesseract.hermes.Suggestion;

public class SuggestionUtils {

    public static MessageEmbed computeEmbedMessage(final Suggestion suggestion)
    {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(suggestion.getTitle())
                    .setColor(suggestion.getStatus().getColor())
                    .setFooter("Proposé par " + suggestion.getDiscordMember().getUser().getAsTag())
                    .addField("État", suggestion.getStatus().getDesc(), false)
                    .addField("Trello id", suggestion.getTrelloCard().getShortLink(), false)
                    .addField("Trello url", suggestion.getTrelloCard().getShortUrl(), false)
                    .setDescription(suggestion.getDescription());

        return embedBuilder.build();
    }
}
