package onl.tesseract.hermes.suggestion;

public enum SuggestionApprovalType {
    FOR_LATER(TrelloList.APPROVED_PENDING),
    IN_DISCUSSION(TrelloList.IN_DISCUSSION),
    DISCUSSED(TrelloList.DISCUSSED),
    ;

    private final TrelloList trelloList;

    SuggestionApprovalType(final TrelloList trelloList)
    {
        this.trelloList = trelloList;
    }

    public TrelloList getTrelloList()
    {
        return trelloList;
    }
}
