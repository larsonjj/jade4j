package de.neuland.jade4j.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Split arguments passed as single String into list of strings, preserve quotes when argument is not simple string constant.
 * For example:
 * foo('a'),'b' -> [ "foo('a')", "b" ]
 *
 * @author dusan.zatkovsky, 2/5/15
 */
public class ArgumentSplitter {

    private static final char argumentDelimiter = ',';
    private final String arguments;
    private List<String> argList = new ArrayList<String>();

    /**
     * Split arguments passed as single String into list
     * @param arguments
     * @return  Parsed arguments
     */
    public static List<String> split(String arguments) {
        return new ArgumentSplitter(arguments).splitArguments();
    }

    private ArgumentSplitter(String arguments) {
        this.arguments = arguments;
    }

    private List<String> splitArguments() {

        final int argLength = arguments.length();
        StringBuilder sb = new StringBuilder(argLength);
        boolean insideQuotas = false;

        for (int i = 0; i < argLength; i++) {
            char ch = arguments.charAt(i);

            // detect when pointer is inside quoted text
            if (ch == '"' || ch == '\'') {
                insideQuotas = !insideQuotas;
            }

            // detect argument delimiter, then push argument
            else if (ch == argumentDelimiter && !insideQuotas) {
                pushArg(sb);
                sb = new StringBuilder(argLength);
            }
            sb.append(ch);
        }
        pushArg(sb);
        return argList;
    }

    private void pushArg(StringBuilder sb) {
        // remove comma, if constant string argument, remove quotes
        String tmp = sb.toString().trim().replaceAll("^,", "").trim();
        for (String s : new String[]{"\"", "'"}) {
            if ( tmp.startsWith(s) && tmp.endsWith(s)) {
                tmp = tmp.substring(1, tmp.length()-1);
                break;
            }
        }
        // add argument to list
        argList.add(tmp);
        // and reset builder for next argument building
        sb = new StringBuilder(arguments.length());
    }

}
