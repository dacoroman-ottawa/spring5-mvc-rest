package guru.springfamework.services;

import java.util.List;

import guru.springfamework.api.v1.model.VendorDTO;

public interface VendorService {

    List<VendorDTO> getAllVendors();

    VendorDTO getVendorById(Long id);

    VendorDTO createNewVendor(VendorDTO vendorDTO);

    VendorDTO saveVendorByDto(Long id, VendorDTO vendorDTO);

    VendorDTO patchVendorByDto(Long id, VendorDTO vendorDTO);

    void deleteVendorById(Long id);
    
}
