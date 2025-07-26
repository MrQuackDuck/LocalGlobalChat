package mrquackduck.localglobalchat.utils;

public class StringBuilderUtil {
    public static void stripLeadingSlashes(StringBuilder messageBuilder) {
        while (!messageBuilder.isEmpty() && messageBuilder.charAt(0) == '/') {
            messageBuilder.deleteCharAt(0);
        }
    }
}
