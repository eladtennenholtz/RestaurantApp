import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Chef implements Runnable{
    private int chefNumber;
    private BlockingQueue<Order> orderQueue;
    private BlockingQueue<Order> completedOrders;

    public Chef(int chefNumber, BlockingQueue<Order> orderQueue, BlockingQueue<Order> completedOrders) {
        this.chefNumber = chefNumber;
        this.orderQueue = orderQueue;
        this.completedOrders = completedOrders;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Order order = orderQueue.take();
                if (order == null) {
                    continue;
                }
                System.out.println("Chef " + chefNumber + " is preparing an order for Table " + order.getTableNumber());
                int totalPreparationTime = order.getTotalPreparationTimeInMinutes() * 60*1000;
                List<Dish> dishes = order.getDishes();
                for(int i=0;i<dishes.size();i++) {
                    System.out.println("Making " + dishes.get(i).getName());
                }
                Thread.sleep(totalPreparationTime);
                System.out.println("Chef " + chefNumber + " completed the order for Table " + order.getTableNumber());
                completedOrders.put(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}