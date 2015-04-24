import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DistributedLockImpl extends UnicastRemoteObject implements DistributedLock {

  private TimeUnit lockTimeoutUnit = TimeUnit.SECONDS;

  private volatile long token = 0;

  byte[] lock = new byte[0];

  long lockTimeout = 60 * 60;

  long beginLockTime;

  public DistributedLockImpl() throws RemoteException {
    super();
  }

  public DistributedLockImpl(long lockTimeout, TimeUnit lockTimeoutUnit) throws RemoteException {
    super();
    this.lockTimeout = lockTimeout;
    this.lockTimeoutUnit = this.lockTimeoutUnit;
  }

  public long lock() throws TimeoutException {
    return tryLock(0, TimeUnit.MILLISECONDS);
  }

  private boolean isLockTimeout() {
    if (lockTimeout <= 0) {
      return false;
    }
    return (System.currentTimeMillis() - beginLockTime) < lockTimeoutUnit.toMillis(lockTimeout);
  }

  private long getToken() {
    beginLockTime = System.currentTimeMillis();
    token = System.nanoTime();
    return token;
  }

  public long tryLock(long time, TimeUnit unit) throws TimeoutException {
    synchronized (lock) {
      long startTime = System.nanoTime();
      while (token != 0 && isLockTimeout()) {
        try {
          if (time > 0) {
            long endTime = System.nanoTime();
            if (endTime - startTime >= unit.toMillis(time)) {
              throw new TimeoutException();
            }
          }
          Thread.sleep(1);
        } catch (InterruptedException e) {

        }
      }
      return getToken();
    }
  }

  public void unlock(long token) {
    if (this.token != 0 && token == this.token) {
      this.token = 0;
    } else {
      throw new RuntimeException(token);
    }
  }
}