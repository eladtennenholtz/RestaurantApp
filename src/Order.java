import java.util.List;

public class Order {

    private List<Dish> dishes;
    private int tableNumber;
    private String specialComments;

    public Order(List<Dish>dishes,int tableNumber,String specialComments){
        this.dishes=dishes;
        this.tableNumber=tableNumber;
        this.specialComments=specialComments;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getSpecialComments() {
        return specialComments;
    }
    public int getTotalPreparationTimeInMinutes() {
        int totalPreparationTimeInMinutes = 0;
        for (Dish dish : dishes) {
            totalPreparationTimeInMinutes += dish.getPreparationTime();
        }
        return totalPreparationTimeInMinutes;
    }
}
