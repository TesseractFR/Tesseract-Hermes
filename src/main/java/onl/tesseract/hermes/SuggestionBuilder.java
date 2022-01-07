package onl.tesseract.hermes;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class SuggestionBuilder {

    private String title;
    private String description;
    private Member discordMember;
    private Message responseMessage;

    public Suggestion build()
    {
        if (title == null || description == null || discordMember == null)
            throw new IllegalStateException();
        return new Suggestion(title, description, discordMember, null, null);
    }

    public SuggestionBuilder setTitle(final String title)
    {
        this.title = title;
        return this;
    }

    public SuggestionBuilder setDescription(final String description)
    {
        this.description = description;
        return this;
    }

    public SuggestionBuilder setDiscordMember(final Member discordMember)
    {
        this.discordMember = discordMember;
        return this;
    }

    public SuggestionBuilder setResponseMessage(final Message responseMessage)
    {
        this.responseMessage = responseMessage;
        return this;
    }
}
