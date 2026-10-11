package carsharing;

import java.util.List;

public class CompanyDOAImpl implements CompanyDOA{
    DbClient dbClient;
    private final String INSERT_DATA;
    private final String SELECT_ALL;

    public CompanyDOAImpl(DbClient dbClient){
        this.dbClient = dbClient;

        INSERT_DATA = "INSERT INTO COMPANY"
                + " (name) VALUES (?)";
        SELECT_ALL = "SELECT * FROM COMPANY";
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