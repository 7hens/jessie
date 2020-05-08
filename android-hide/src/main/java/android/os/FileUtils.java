package android.os;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class FileUtils {
    public static int setPermissions(File path, int mode, int uid, int gid) {
        return setPermissions(path.getAbsolutePath(), mode, uid, gid);
    }

    public static int setPermissions(String path, int mode, int uid, int gid) {
        return -1;
    }

    public static int setPermissions(FileDescriptor fd, int mode, int uid, int gid) {
        return -1;
    }

    public static void copyPermissions(File from, File to) throws IOException {
    }

    public static int getUid(String path) {
        return -1;
    }
}
