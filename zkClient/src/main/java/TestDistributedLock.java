import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
public class TestDistributedLock {
    
    public static void main(String[] args) {

        final int connectionTimeout=60*1000;

        final ZkClientExt zkClientExt1 = new ZkClientExt("192.168.3.157:2181", connectionTimeout,
                5000, new BytesPushThroughSerializer());

        final SimpleDistributedLockMutex mutex1 = new SimpleDistributedLockMutex(zkClientExt1, "/Mutex");
        
        final ZkClientExt zkClientExt2 = new ZkClientExt("192.168.3.157:2181", connectionTimeout,
                5000, new BytesPushThroughSerializer());
        final SimpleDistributedLockMutex mutex2 = new SimpleDistributedLockMutex(zkClientExt2, "/Mutex");
        
        try {
            mutex1.acquire();
            System.out.println("Client1 locked");
            Thread client2Thd = new Thread(new Runnable() {
                
                public void run() {
                    try {
                        mutex2.acquire();
                        System.out.println("Client2 locked1");
                        mutex2.release();
                        System.out.println("Client2 released lock1");
                        mutex2.acquire();
                        System.out.println("Client2 locked2");
                        mutex2.release();
                        System.out.println("Client2 released lock2");
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                
                }
            });
            client2Thd.start();
            Thread.sleep(5000);
            mutex1.release();            
            System.out.println("Client1 released lock");
            
            client2Thd.join();
            
        } catch (Exception e) {
 
            e.printStackTrace();
        }
        
    }
 
}