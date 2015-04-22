
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is thread safe.
 */
public class Parser {
    
  private static final int UNICODE_THRESHOLD = 0x80;
  private final ReadWriteLock fileLock = new ReentrantReadWriteLock();
  private File file;
    
  public final void setFile(final File newFile) {
    this.fileLock.writeLock().lock();
    try {
      this.file = newFile;
    } finally {
      this.fileLock.writeLock().unlock();
    }
  }
    
  public final File getFile() {
    this.fileLock.readLock().lock();
    try {
      return this.file;
    } finally {
      this.fileLock.readLock().unlock();
    }
  }
    
  public final String getContent() throws IOException {
    final FileInputStream input = new FileInputStream(this.getFile());
    try {
      final StringBuffer output = new StringBuffer();
      int data = input.read();
      while (data > 0) {
        output.append((char) data);
        data = input.read();
      }
      return output.toString();
    } finally {
      input.close();
    }
  }
    
  public final String getContentWithoutUnicode() throws IOException {
    final FileInputStream input = new FileInputStream(this.getFile());
    try {
      final StringBuffer output = new StringBuffer();
      int data = input.read();
      while (data > 0) {
        if (data < UNICODE_THRESHOLD) {
          output.append((char) data);
        }
        data = input.read();
      }
      return output.toString();
    } finally {
      input.close();
    }
  }
    
  public final void saveContent(final String content) throws IOException {
    final FileOutputStream output = new FileOutputStream(this.getFile());
    try {
      for (int index = 0; index < content.length(); index += 1) {
        output.write(content.charAt(index));
      }
    } finally {
      output.close();
    }
  }
}
