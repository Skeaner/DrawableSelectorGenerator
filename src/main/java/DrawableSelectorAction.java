import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.apache.commons.lang3.StringUtils;
import utils.FileNameUtils;
import utils.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public class DrawableSelectorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Log.d("---- Start - menu item clicked ----");
        String errorMsg = null;
        VirtualFile[] selectedFiles = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selectedFiles ==null || selectedFiles.length==0){
            errorMsg = "No file or directory is selected!";
        }
        else {
            if (selectedFiles.length==1){
                VirtualFile selectedFile = selectedFiles[0];
                Log.d("selected file : " + selectedFile.getName());
                if (selectedFile.isDirectory() && isCorrectFolderSelected(selectedFile)) {
                    new FolderDetector(e.getProject()).detectAndCreateSelectors(selectedFile);
                } else {
                    errorMsg = "You need to select folder with image resources!";
                }
            }
            else {
                String[] allNames = new String[selectedFiles.length];
                for (int i = 0; i < selectedFiles.length; i++) {
                    allNames[i] = selectedFiles[i].getName();
                }
                Log.d("selected files : [ " + StringUtils.join(allNames, " , ") + " ]");
                if (isCorrectFilesSelected(selectedFiles)) {
                    new FilesDetector(e.getProject()).detectAndCreateSelector(selectedFiles);
                }
                else {
                    errorMsg = "You need to select image files with same prefix and validate suffix!";
                }
            }
        }

        if (errorMsg == null) {
            showInfoDialog("Selectors were generated into 'drawable' folder", e);
        } else {
            showErrorDialog(errorMsg, e);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        VirtualFile[] selectedFiles = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selectedFiles ==null || selectedFiles.length==0){
            e.getPresentation().setEnabled(false);
        }
        else {
            if (selectedFiles.length==1){
                VirtualFile selectedFile = selectedFiles[0];
                Log.d("selected file : " + selectedFile.getName());
                if (selectedFile.isDirectory()) {
                    e.getPresentation().setEnabled(isCorrectFolderSelected(selectedFile));
                } else {
                    e.getPresentation().setEnabled(false);
                }
            }
            else {
                String[] allNames = new String[selectedFiles.length];
                for (int i = 0; i < selectedFiles.length; i++) {
                    allNames[i] = selectedFiles[i].getName();
                }
                Log.d("selected files : [ " + StringUtils.join(allNames, " , ") + " ]");
                e.getPresentation().setEnabled(isCorrectFilesSelected(selectedFiles));
            }
        }
    }

    private boolean isCorrectFolderSelected(VirtualFile selectedFile) {
        Matcher matcher = Constants.VALID_FOLDER_PATTERN.matcher(selectedFile.getName());
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    private boolean isCorrectFilesSelected(VirtualFile[] files) {
        Set<String> prefixs = new HashSet<>();
        List<String> suffixs = new ArrayList<>();
        LoopFile:
        for (VirtualFile file : files) {
            String name = FileNameUtils.removeFileEndings(file.getName());
            for (String suffix : Constants.SUFFIXES) {
                if (name.contains(suffix)) {
                    suffixs.add(suffix);
                    prefixs.add(name.replace(suffix, ""));
                    continue LoopFile;
                }
            }
        }
        return prefixs.size() == 1 && suffixs.size() >= 2;
    }

    private void showInfoDialog(String text, AnActionEvent e) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(PlatformDataKeys.PROJECT.getData(e.getDataContext()));

        if (statusBar != null) {
            JBPopupFactory.getInstance()
                          .createHtmlTextBalloonBuilder(text, MessageType.INFO, null)
                          .setFadeoutTime(10000)
                          .createBalloon()
                          .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
        }
    }

    private void showErrorDialog(String text, AnActionEvent e) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(PlatformDataKeys.PROJECT.getData(e.getDataContext()));

        if (statusBar != null) {
            JBPopupFactory.getInstance()
                          .createHtmlTextBalloonBuilder(text, MessageType.ERROR, null)
                          .setFadeoutTime(10000)
                          .createBalloon()
                          .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
        }
    }
}
