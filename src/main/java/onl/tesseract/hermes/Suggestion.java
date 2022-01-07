package onl.tesseract.hermes;

import com.julienvey.trello.domain.Card;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import onl.tesseract.hermes.suggestion.SuggestionStatus;
import onl.tesseract.hermes.suggestion.TrelloList;
import org.springframework.lang.Nullable;

import java.util.Objects;

public final class Suggestion {
    private final String title;
    private final String description;
    private final Member discordMember;
    private Message responseMessage;
    private Card trelloCard;
    private SuggestionStatus status;

    public Suggestion(String title, String description, Member discordMember,
                      Message responseMessage, Card trelloCard)
    {
        this.status = SuggestionStatus.CREATED;
        this.title = title;
        this.description = description;
        this.discordMember = discordMember;
        this.responseMessage = responseMessage;
        this.trelloCard = trelloCard;
    }

    public void submit()
    {
        Card card = new Card();
        card.setName(title);
        card.setDesc(buildDescription());
        this.trelloCard = TrelloList.SUGGESTIONS.getList().createCard(card);
        this.status = SuggestionStatus.PENDING;
    }

    private String buildDescription()
    {
        String content = "Auteur : "
                + discordMember.getUser().getAsTag();
        if (responseMessage != null)
            content += "\nRéponse : " + responseMessage.getId()
                    + "\nLien de la réponse : " + responseMessage.getJumpUrl();
        content += "\n\n"
                + description;
        return content;
    }

    public void updateTrelloCard()
    {
        this.trelloCard.setDesc(buildDescription());
        this.trelloCard = this.trelloCard.update();
    }

    public void setResponseMessage(final Message responseMessage)
    {
        this.responseMessage = responseMessage;
        updateTrelloCard();
    }

    public SuggestionStatus getStatus()
    {
        return status;
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

    @Nullable
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
