package carsharing;

import java.util.List;

public class CompanyDAOImpl implements CompanyDAO{

    DbClient dbClient;
    private final String INSERT_DATA = "INSERT INTO COMPANY"
            + " (name) VALUES (?)";;
    private final String SELECT_ALL = "SELECT * FROM COMPANY";;

    public CompanyDAOImpl(DbClient dbClient){
        this.dbClient = dbClient;
    }

    @Override
    public boolean add(Company company) {
        return dbClient.addCompany(INSERT_DATA, company.getName());
    }

    @Override
    public List<Company> findAll() {
        return dbClient.getAllCompanies(SELECT_ALL);
    }

}