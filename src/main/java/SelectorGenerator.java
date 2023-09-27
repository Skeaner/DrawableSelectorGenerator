import com.intellij.openapi.vfs.VirtualFile;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import utils.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SelectorGenerator {

    private static final String SCHEMA = "http://schemas.android.com/apk/res/android";
    private static final String NS = "android";

    public static void generate(VirtualFile newFile, String prefix, List<String> suffixs) {
        Log.d("generating XML:");
        Element root = new Element("selector");
        root.addNamespaceDeclaration(NS, SCHEMA);
        boolean containNormal = false;
        for (String suffix : suffixs) {
            if (Constants.NORMAL.equals(suffix)) {
                containNormal = true;
            } else {
                String drawableName = prefix + suffix ;
                String state = Constants.sMapping.get(suffix);
                Log.d("fileName=" +drawableName + ", states:" + state);
                Element item = new Element("item");
                Attribute attribute = new Attribute("drawable", "@drawable/" + drawableName);
                attribute.setNamespace(NS, SCHEMA);
                item.addAttribute(attribute);
                addState(item, state, true);
                Log.d("row=" + item.toXML());
                root.appendChild(item);
            }
        }
        if (containNormal){
            String drawableName = prefix + Constants.NORMAL ;
            Log.d("fileName=" +drawableName );
            Element item = new Element("item");
            Attribute attribute = new Attribute("drawable", "@drawable/" + drawableName);
            attribute.setNamespace(NS, SCHEMA);
            item.addAttribute(attribute);
            Log.d("row=" + item.toXML());
            root.appendChild(item);
        }
        Document doc = new Document(root);
        OutputStream os = null;
        try {
            os = newFile.getOutputStream(null);
            Serializer serializer = new Serializer(os);
            serializer.setIndent(4);
            serializer.write(doc);
        } catch (IOException e) {
            Log.e(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(e);
                }
            }
        }

    }

    private static void addState(Element item, String state, boolean value) {
        Attribute attribute = new Attribute(state, String.valueOf(value));
        attribute.setNamespace(NS, SCHEMA);
        item.addAttribute(attribute);
    }
}
