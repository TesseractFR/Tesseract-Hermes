package onl.tesseract.hermes.exception;

public class SuggestionParsingException extends Exception {

    public SuggestionParsingException()
    {
    }

    public SuggestionParsingException(final String message)
    {
        super(message);
    }

    public SuggestionParsingException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public SuggestionParsingException(final Throwable cause)
    {
        super(cause);
    }

    public SuggestionParsingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
