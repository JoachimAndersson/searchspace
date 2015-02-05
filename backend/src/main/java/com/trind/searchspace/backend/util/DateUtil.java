package  com.trind.searchspace.backend.util;

import  com.trind.searchspace.backend.exception.DateTimeFormatException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Joachim on 2014-10-23.
 */
public class DateUtil {

    public static LocalDateTime parse(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        LocalDateTime time;
        String mathString;
        if (text.startsWith("now")) {
            time = LocalDateTime.now();
            mathString = text.substring("now".length());
        } else {
            int index = text.indexOf("||");
            String parseString;
            if (index == -1) {
                parseString = text;
                mathString = ""; // nothing else
            } else {
                parseString = text.substring(0, index);
                mathString = text.substring(index + 2);
            }
            time = parseStringValue(parseString);
        }

        if (mathString.isEmpty()) {
            return time;
        }

        return parseMath(mathString, time);
    }

    private enum Operator {

        PLUS('+'), MINUS('-');

        private char value;

        Operator(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    private static LocalDateTime parseMath(String mathString, LocalDateTime localDateTime) {
        try {
            for (int i = 0; i < mathString.length(); ) {
                char c = mathString.charAt(i++);
                Operator type;
                if (c == Operator.PLUS.getValue()) {
                    type = Operator.PLUS;
                } else if (c == Operator.MINUS.getValue()) {
                    type = Operator.MINUS;
                } else {
                    throw new DateTimeFormatException();
                }

                int num;
                if (!Character.isDigit(mathString.charAt(i))) {
                    num = 1;
                } else {
                    int numFrom = i;
                    while (Character.isDigit(mathString.charAt(i))) {
                        i++;
                    }
                    num = Integer.parseInt(mathString.substring(numFrom, i));
                }
                char unit = mathString.charAt(i++);
                switch (unit) {
                    case 'y':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusYears(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusYears(num);
                        }
                        break;
                    case 'M':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusMonths(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusMonths(num);
                        }
                        break;
                    case 'w':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusWeeks(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusWeeks(num);
                        }
                        break;
                    case 'd':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusDays(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusDays(num);
                        }
                        break;
                    case 'h':
                    case 'H':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusHours(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusHours(num);
                        }
                        break;
                    case 'm':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusMinutes(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusMinutes(num);
                        }
                        break;
                    case 's':
                        if (type == Operator.PLUS) {
                            localDateTime = localDateTime.plusSeconds(num);
                        } else if (type == Operator.MINUS) {
                            localDateTime = localDateTime.minusSeconds(num);
                        }
                        break;
                    default:
                        throw new DateTimeFormatException();
                }
            }
        } catch (Exception e) {
            throw new DateTimeFormatException();
        }
        return localDateTime;
    }

    private static LocalDateTime parseStringValue(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
        return dateTime;
    }


}
