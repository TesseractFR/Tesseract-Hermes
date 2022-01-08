package onl.tesseract.hermes.suggestion;

import com.julienvey.trello.domain.Card;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import onl.tesseract.hermes.Suggestion;
import onl.tesseract.hermes.exception.SuggestionParsingException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SuggestionCardParser {
    private static final Pattern HEADER_PATTERN = Pattern.compile("(.*) : (.*)");

    private final Guild guild;
    private final TextChannel suggestionChannel;

    public SuggestionCardParser(final Guild guild, final TextChannel suggestionChannel)
    {
        this.guild = guild;
        this.suggestionChannel = suggestionChannel;
    }

    public Suggestion parse(final Card card) throws SuggestionParsingException
    {
        String description = card.getDesc();

        int delimiterPos = description.indexOf("%%");
        if (delimiterPos == -1)
            throw new SuggestionParsingException("No delimiter found for card " + card.getShortUrl());
        String header = description.substring(0, delimiterPos);
        String content = description.substring(delimiterPos + 2);

        // Parse header
        Map<String, String> headerFields = parseHeaderFields(header);
        final String authorId = headerFields.get("AuthorId");
        final String replyId = headerFields.get("Réponse");
        if (authorId == null || replyId == null)
            throw new SuggestionParsingException("Missing headers for card " + card.getShortUrl());

        Member member = guild.retrieveMemberById(authorId).complete();
        Message reply = suggestionChannel.retrieveMessageById(Long.parseLong(replyId))
                                         .complete();
        if (member == null)
            throw new SuggestionParsingException("Member not found for id " + authorId);
        if (reply == null)
            throw new SuggestionParsingException("Reply message not found for id " + replyId);

        return new Suggestion(card.getName(), content, member, reply, card);
    }

    private Map<String, String> parseHeaderFields(final String header)
    {
        Map<String, String> headers = new HashMap<>();

        String[] headerFields = header.split("\n");
        for (final String headerField : headerFields)
        {
            Matcher matcher = HEADER_PATTERN.matcher(headerField);
            if (!matcher.matches())
                continue;
            headers.put(matcher.group(1), matcher.group(2));
        }

        return headers;
    }
}
