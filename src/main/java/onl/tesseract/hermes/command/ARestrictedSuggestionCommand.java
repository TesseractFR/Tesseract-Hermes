package onl.tesseract.hermes.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import onl.tesseract.hermes.suggestion.SuggestionCardParser;

import java.awt.*;

public abstract class ARestrictedSuggestionCommand extends ASpecificSuggestionCommand {

    public ARestrictedSuggestionCommand(final SuggestionCardParser suggestionCardParser)
    {
        super(suggestionCardParser);
    }

    @Override
    public void execute(final SlashCommandEvent event)
    {
        if (event.getMember() == null)
            throw new IllegalStateException("Event has no member");
        final boolean hasAccess = event.getMember().getRoles()
                                       .stream()
                                       .anyMatch(role -> role.getName().equals("Trello"));
        if (hasAccess)
            super.execute(event);
        else
        {
            getLogger().info("Access to command '{}' denied for user {}", name(), event.getMember().getUser().getAsTag());
            event.replyEmbeds(new EmbedBuilder()
                         .setTitle(":x: Accès refusé :x:")
                         .setDescription("Tu n'as pas la permission d'exécuter cette commande")
                         .setColor(new Color(192, 77, 77))
                         .build())
                 .setEphemeral(true)
                 .queue();
        }
    }
}
