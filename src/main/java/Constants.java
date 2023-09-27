import java.util.HashMap;
import java.util.regex.Pattern;

public class Constants {
    public static final String NORMAL = "_normal";
    public static final String FOCUSED = "_focused";
    public static final String PRESSED = "_pressed";
    public static final String SELECTED = "_selected";
    public static final String CHECKED = "_checked";
    public static final String DISABLED = "_disabled";
    public static final String HOVERED = "_hovered";
    public static final String CHECKABLE = "_checkable";
    public static final String ACTIVATED = "_activated";
    public static final String WINDOW_FOCUSED = "_windowfocused";
    public static String[] SUFFIXES = new String[]{NORMAL,
                                                   PRESSED,
                                                   FOCUSED,
                                                   SELECTED,
                                                   CHECKED,
                                                   DISABLED,
                                                   HOVERED,
                                                   CHECKABLE,
                                                   ACTIVATED,
                                                   WINDOW_FOCUSED};
    public static Pattern VALID_FOLDER_PATTERN = Pattern.compile("^drawable(-[a-zA-Z0-9]+)*$");
    public static String EXPORT_FOLDER = "drawable";
    public static HashMap<String, String> sMapping;

    static {
        // mapping from file suffixes into android attributes and their default values
        sMapping = new HashMap<String, String>();
        sMapping.put(FOCUSED, "state_focused");
        sMapping.put(PRESSED, "state_pressed");
        sMapping.put(SELECTED, "state_selected");
        sMapping.put(CHECKED, "state_checked");
        sMapping.put(DISABLED, "state_enabled");
        sMapping.put(HOVERED, "state_hovered");
        sMapping.put(CHECKABLE, "state_checkable");
        sMapping.put(ACTIVATED, "state_activated");
        sMapping.put(WINDOW_FOCUSED, "state_window_focused");
    }

}
