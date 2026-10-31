package carsharing;

public class Car {

    private int id;
    private int company_id;
    private String name;

    //for adding just company name, id is auto incremented
    public Car(int company_id, String name){
        this.company_id = company_id;
        this.name = name;
    }

    //for retrieving all the car info
    public Car(int id, String name, int company_id ){
        this.id = id;
        this.name = name;
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return company_id;
    }

    public int getId() {
        return id;
    }

}