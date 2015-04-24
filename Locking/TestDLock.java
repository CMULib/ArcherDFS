import java.rmi.Remote;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class TestDLock {
  public static void main(String[] args) throws Exception {
    RmiServer rmiServer = new LocalRmiServer();
    DistributedLockImpl distributedLock = new DistributedLockImpl();
    rmiServer.registerRemoteObject("lock1", distributedLock);
    MultiThreadProcessor processor = new MultiThreadProcessor("aa");
    for (int i = 0; i < 8; i++) {
      processor.addProcessor(new RunLock("aa" + i));
    }
    long s = System.currentTimeMillis();
    processor.start();
    long e = System.currentTimeMillis();
    System.out.println(e - s);
    rmiServer.unexportObject(distributedLock);
  }
}

class RunLock extends AbstractProcessor {
  public RunLock(String name) {
    super(name);
  }

  @Override
  protected void action() throws Exception {
    try {
      RmiServer client = new RemoteRmiServer();
      DistributedLock lock = client.getRemoteObject("lock1");
      for (int i = 0; i < 1000; i++) {
        long token = lock.lock();
        lock.unlock(token);
      }
      System.out.println("end-" + Thread.currentThread().getId());
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment arg1) {
    // TODO Auto-generated method stub
    return false;
  }
}