package carsharing;

public class Company {

    int id;
    String name;

    //for adding just company name, id is auto incremented
    public Company (String name){
        this.name = name;
    }

    //for retrieving the id and the company name when displaying list of companies
    public Company (int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}