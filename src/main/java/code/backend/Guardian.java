package code.backend;

enum ErrorType
{
    DAYS_INT_OVERFLOW,
    DAYS_INT_UNDERFLOW,
    FOLDER_NAME_CONFLICT
}

public class Guardian
{
    private Guardian() {}

    public static boolean integerCanFit(long test)
    {
        if (test > (long) Integer.MAX_VALUE)
            {
                return false;
            }
        else if (test < (long) Integer.MIN_VALUE)
            {
                return false;
            }
        return true;
    }

    // todo: method should call frontend to display errors
    public static void triggerErrorMsg(ErrorType err)
    {
        switch (err)
            {
            case ErrorType.DAYS_INT_OVERFLOW:
                System.err.println("dude, that's too far into the future... we'd all be dead by then");
                break;
            case ErrorType.DAYS_INT_UNDERFLOW:
                System.err.println("i know nostalgia is strong, but that's a little too far back");
                break;
            case ErrorType.FOLDER_NAME_CONFLICT:
                System.err.println("another folder already has that name?!");
                break;
            default:
                System.err.println("an unknown error has occured");
                break;
            }
    }
}
