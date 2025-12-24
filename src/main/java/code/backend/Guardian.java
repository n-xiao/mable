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
                triggerErrorMsg(ErrorType.DAYS_INT_OVERFLOW);
                return false;
            }
        else if (test < (long) Integer.MIN_VALUE)
            {
                triggerErrorMsg(ErrorType.DAYS_INT_UNDERFLOW);
                return false;
            }
        return true;
    }

    // todo: method should call frontend to display errors
    private static String triggerErrorMsg(ErrorType err)
    {
        switch (err)
            {
            case ErrorType.DAYS_INT_OVERFLOW:
                return "dude, that's too far into the future... we'd all be dead by then";
            case ErrorType.DAYS_INT_UNDERFLOW:
                return "i know nostalgia is strong, but that's a little too far back";
            case ErrorType.FOLDER_NAME_CONFLICT:
                return "another folder already has that name?!";

            default:
                return "";
            }
    }
}
