import java.io.File;

public class Locking {
  public static int create() {
    String filePath = "lock";
    try {
      File file = new File(filePath);
      if (file.exists() && file.isFile()) {
        System.out.println("locking");
        return 0;
      } else {
        file.createNewFile();
        System.out.println("lock created");
        return 1;
      }
    } catch (Exception e) {
      System.out.println("create lock error");
      return -1;
    }
  }

  public static int lock() {
    while (create() != 1) {
    }
    return 1;
  }

  public static int release() {
    String filePath = "lock";
    File file = new File(filePath);
    if (file.exists() && file.isFile()) {
      if (file.delete()) {
        System.out.println("release done");
        return 1;
      } else {
        System.out.println("release fail");
        return 0;
      }
    } else {
      System.out.println("release error");
      return -1;
    }
  }

  public static void main(String[] args) {
    lock();
    release();
  }
}
