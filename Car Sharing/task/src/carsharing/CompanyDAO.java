package carsharing;

import java.util.List;

public interface CompanyDAO {
    boolean add(Company company);
    List<Company> findAll();
}