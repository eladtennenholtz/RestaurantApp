import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Waitress implements Runnable{

    //private BlockingQueue<Order> orderQueue;
    private BlockingQueue<Order> completedOrders;
    private int bill;
    private Runnable updateSum;

    public Waitress(BlockingQueue<Order> completedOrders) {
        //  this.orderQueue = orderQueue;
        this.completedOrders = completedOrders;
    }
    synchronized public void updateBillOfTable(int bill){
        this.bill=bill;
    }


    //    public void placeOrder(Order order) {
//        try {
//            orderQueue.put(order);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
    @Override
    public void run() {

        while (true) {
            try {
                Order completedOrder = completedOrders.take();
                System.out.println("Waiter is delivering the order to Table " + completedOrder.getTableNumber());
                Thread.sleep(2000);
                System.out.println("Order delivered to Table " + completedOrder.getTableNumber());
                List<Dish> dishList =completedOrder.getDishes();
                int sum=0;
                for(int i=0;i<dishList.size();i++){
                    sum+=dishList.get(i).getPrice();
                }
                updateBillOfTable(sum);
                System.out.println("The bill of this table is $"+sum);


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}