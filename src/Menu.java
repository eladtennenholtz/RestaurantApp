import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<Dish> allDishes;

    public Menu(){
        allDishes = new ArrayList<>();
    }

    public List<Dish> getAllDishes() {
        return allDishes;
    }

    public void showMenu(){
        System.out.println("Menu of Restaurant: ");
        for (int i = 0; i < allDishes.size(); i++) {
            Dish dish = allDishes.get(i);
            System.out.println((i + 1) + ". " + dish.getName() + " - $" + dish.getPrice());
        }
    }

    public void addNewDish(String nameOfDish, double priceOfDish,int preparationTime){
        Dish addedDish = new Dish(nameOfDish,priceOfDish,preparationTime);
        allDishes.add(addedDish);
    }
}
