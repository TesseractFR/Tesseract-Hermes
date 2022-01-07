package onl.tesseract.hermes.suggestion;

public enum SuggestionStatus {
    CREATED("En création..."),
    PENDING("En attente"),
    APPROVED("Acceptée"),
    REFUSED("Refusée"),
    ;

    private final String desc;

    SuggestionStatus(final String desc)
    {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }
}
