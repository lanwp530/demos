package per.lwp.thread.pc;


/**
 * 生产者消费者测试
 *  有休眠 wait() notify()
 * created by lawnp on 2018/12/19.
 */
public class ProducerConsumer2Test {
    public static void main(String[] args) {
        Store2 store = new Store2();
        createProducerTread(store, "生成者-1");
        createConsumerTread(store, "消费者-11");
        createConsumerTread(store, "消费者-22");
        createConsumerTread(store, "消费者-33");

    }

    public static void createProducerTread(Store2 store, String name) {
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
        }, name).start();
    }

    public static void createConsumerTread(Store2 store, String name) {
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
        }, name).start();
    }
}
