/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.backend;

enum ErrorType { DAYS_INT_OVERFLOW, DAYS_INT_UNDERFLOW, FOLDER_NAME_CONFLICT }

public class Guardian {
    private Guardian() {}

    public static boolean integerCanFit(long test) {
        if (test > (long) Integer.MAX_VALUE) {
            return false;
        } else if (test < (long) Integer.MIN_VALUE) {
            return false;
        }
        return true;
    }

    // todo: method should call frontend to display errors
    public static void triggerErrorMsg(ErrorType err) {
        switch (err) {
            case ErrorType.DAYS_INT_OVERFLOW:
                System.err.println(
                    "dude, that's too far into the future... we'd all be dead by then");
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
