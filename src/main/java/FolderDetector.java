import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import utils.FileNameUtils;
import utils.RunnableHelper;

import java.util.*;

public class FolderDetector {

    private Project mProject;

    public FolderDetector(Project project) {
        mProject = project;
    }

    public void detectAndCreateSelectors(VirtualFile selectedFolder) {
        HashMap<String, ArrayList<String>> dataMaps = new HashMap<>();
        VirtualFile[] children = selectedFolder.getChildren();
        LoopFile:
        for (VirtualFile child : children) {
            String name = FileNameUtils.removeFileEndings(child.getName());
            for (String suffix : Constants.SUFFIXES) {
                if (name.contains(suffix)) {
                    String prefix = name.replace(suffix, "");
                    ArrayList<String> suffixs;
                    if (dataMaps.containsKey(prefix)) {
                        suffixs = dataMaps.get(prefix);
                    } else {
                        suffixs = new ArrayList<>();
                    }
                    suffixs.add(suffix);
                    dataMaps.put(prefix, suffixs);
                    continue LoopFile;
                }
            }
        }
        dataMaps.entrySet().removeIf(entry -> entry.getValue().size() < 2);
        createFiles(selectedFolder, dataMaps);
    }

    private void createFiles(VirtualFile selectedDrawableFolder, HashMap<String, ArrayList<String>> dataMaps) {
        RunnableHelper.runReadCommand(mProject, () -> {
            try {
                VirtualFile resFolder = selectedDrawableFolder.getParent();
                if (resFolder == null) return;
                VirtualFile drawableFolder = resFolder.findChild(Constants.EXPORT_FOLDER);
                if (drawableFolder == null || !drawableFolder.exists()) {
                    drawableFolder = resFolder.createChildDirectory(null, Constants.EXPORT_FOLDER);
                }
                for (Map.Entry<String, ArrayList<String>> e : dataMaps.entrySet()) {
                    String fileName = e.getKey() + "_selector.xml";
                    VirtualFile selectorFile = drawableFolder.findChild(fileName);
                    if (selectorFile != null && selectorFile.exists()) {
                        selectorFile.delete(null);
                    }
                    selectorFile = drawableFolder.createChildData(null, fileName);
                    SelectorGenerator.generate(selectorFile, e.getKey(), e.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
