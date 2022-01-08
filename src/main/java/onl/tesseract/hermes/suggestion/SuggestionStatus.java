package onl.tesseract.hermes.suggestion;

import java.awt.*;

public enum SuggestionStatus {
    CREATED("En création...", new Color(126, 140, 148)),
    PENDING("En attente", new Color(52, 152, 219)),
    APPROVED("Acceptée", new Color(49, 203, 55)),
    REFUSED("Refusée", new Color(194, 83, 71)),
    ;

    private final String desc;
    private final Color color;

    SuggestionStatus(final String desc, final Color color)
    {
        this.desc = desc;
        this.color = color;
    }

    public String getDesc()
    {
        return desc;
    }

    public Color getColor()
    {
        return color;
    }
}
