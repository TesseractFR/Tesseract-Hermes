package onl.tesseract.hermes;

import com.julienvey.trello.domain.Card;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import onl.tesseract.hermes.suggestion.TrelloList;

import java.util.Objects;

public final class Suggestion {
    private final String title;
    private final String description;
    private final Member discordMember;
    private final Message responseMessage;
    private Card trelloCard;

    public Suggestion(String title, String description, Member discordMember,
                      Message responseMessage, Card trelloCard)
    {
        this.title = title;
        this.description = description;
        this.discordMember = discordMember;
        this.responseMessage = responseMessage;
        this.trelloCard = trelloCard;
    }

    public void submit()
    {
        this.trelloCard = TrelloList.SUGGESTIONS.getList().createCard(this.trelloCard);
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public Member getDiscordMember()
    {
        return discordMember;
    }

    public Message getResponseMessage()
    {
        return responseMessage;
    }

    public Card getTrelloCard()
    {
        return trelloCard;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Suggestion) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.discordMember, that.discordMember) &&
                Objects.equals(this.responseMessage, that.responseMessage) &&
                Objects.equals(this.trelloCard, that.trelloCard);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, description, discordMember, responseMessage, trelloCard);
    }

    @Override
    public String toString()
    {
        return "Suggestion[" +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "discordMember=" + discordMember + ", " +
                "responseMessage=" + responseMessage + ", " +
                "trelloCard=" + trelloCard + ']';
    }

}
