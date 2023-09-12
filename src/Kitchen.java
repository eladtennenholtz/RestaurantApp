import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Kitchen {

    private int numOfChefs;
    private ExecutorService chefsExecutor;
    private BlockingQueue<Order> orderQueue;
    private BlockingQueue<Order> completedOrders;

    public Kitchen(int numOfChefs,BlockingQueue<Order> orderQueue,BlockingQueue<Order> completedOrders){
        this.numOfChefs = numOfChefs;
        chefsExecutor = Executors.newFixedThreadPool(numOfChefs);
        this.orderQueue=orderQueue;
        this.completedOrders = completedOrders;
        for (int i = 0; i < numOfChefs; i++) {
            chefsExecutor.execute(new Chef(i + 1, orderQueue,completedOrders));
        }
    }

    public void submitOrder(Order order) {
        try {
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        chefsExecutor.shutdown();
    }
}
