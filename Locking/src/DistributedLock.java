import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface DistributedLock extends RemoteObject {
 
    long lock() throws RemoteException, TimeoutException;
 
    long tryLock(long time, TimeUnit unit) throws RemoteException, TimeoutException;
 
    void unlock(long token) throws RemoteException;
 
}