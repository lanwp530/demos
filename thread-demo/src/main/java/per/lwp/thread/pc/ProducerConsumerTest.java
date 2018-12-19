package per.lwp.thread.pc;


/**
 * 生产者消费者测试
 * 无休眠 wait() notify()
 * created by lawnp on 2018/12/19.
 */
public class ProducerConsumerTest {
    public static void main(String[] args) {
        Store store = new Store();
        createBuyTread(store);
        createSellTread(store);

    }

    public static void createBuyTread(Store store) {
        new Thread(() -> {
            boolean flag = true;
            while (flag) {
                // 出货
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                store.buy();
            }
        }).start();
    }

    public static void createSellTread(Store store) {
        new Thread(() -> {
            boolean flag = true;
            while (flag) {
                // 出货
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                store.sell();
            }
        }).start();
    }
}
