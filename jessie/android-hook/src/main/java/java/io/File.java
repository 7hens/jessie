package java.io;

import java.net.URI;

public class File extends FileHook {
    public File(String pathname) {
        super(pathname);
    }

    public File(String parent, String child) {
        super(parent, child);
    }

    public File(File parent, String child) {
        super(parent, child);
    }

    public File(URI uri) {
        super(uri);
    }
}
