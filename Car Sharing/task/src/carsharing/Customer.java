package carsharing;

public class Customer {
    int id;
    String name;
    int rented_car_id;

    public Customer(String name){
        this.name = name;
    }

    public Customer(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRented_car_id() {
        return rented_car_id;
    }
}
