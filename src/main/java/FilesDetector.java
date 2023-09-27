import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import utils.FileNameUtils;
import utils.Log;
import utils.RunnableHelper;

import java.io.IOException;
import java.util.*;

public class FilesDetector {

    private Project mProject;
    private VirtualFile[] targetFiles;

    public FilesDetector(Project mProject) {
        this.mProject = mProject;
    }

    public void detectAndCreateSelector(VirtualFile[] targetFiles) {
        this.targetFiles = targetFiles;
        HashSet<String> prefixs = new HashSet<>();
        List<String> suffixs = new ArrayList<>();
        LoopFile:
        for (VirtualFile file : targetFiles) {
            String name = FileNameUtils.removeFileEndings(file.getName());
            for (String suffix : Constants.SUFFIXES) {
                if (name.contains(suffix)) {
                    suffixs.add(suffix);
                    prefixs.add(name.replace(suffix, ""));
                    continue LoopFile;
                }
            }
        }
        if (prefixs.size() == 1 && suffixs.size() >= 1) {
            createFile(prefixs.iterator().next(), suffixs);
        }
    }

    private void createFile(String prefix, List<String> suffixs) {
        RunnableHelper.runReadCommand(mProject, () -> {
            try {
                VirtualFile drawableXXXFolder = targetFiles[0].getParent();
                if (drawableXXXFolder == null) return;
                VirtualFile resFolder = drawableXXXFolder.getParent();
                if (resFolder == null) return;
                VirtualFile drawableFolder = resFolder.findChild(Constants.EXPORT_FOLDER);
                if (drawableFolder == null || !drawableFolder.exists()) {
                    drawableFolder = resFolder.createChildDirectory(null, Constants.EXPORT_FOLDER);
                }
                String fileName = prefix + "_selector.xml";
                VirtualFile selectorFile = drawableFolder.findChild(fileName);
                if (selectorFile != null && selectorFile.exists()) {
                    selectorFile.delete(null);
                }
                selectorFile = drawableFolder.createChildData(null, fileName);
                SelectorGenerator.generate(selectorFile, prefix, suffixs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
