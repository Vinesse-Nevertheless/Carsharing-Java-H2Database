package carsharing;

import java.util.List;

public interface CompanyDOA {
    boolean add(Company company);
    List<Company> findAll();
}