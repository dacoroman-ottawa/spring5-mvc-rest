package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDTO;

import java.util.List;

public interface CustomerService {
    
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

    CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO);


    CustomerDTO patchCustomerByDTO(Long id, CustomerDTO customerDTO);

    void deleteCustomerById(Long id);
    
}
