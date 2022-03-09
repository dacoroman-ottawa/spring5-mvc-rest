package guru.springfamework.services;

import java.util.List;
import java.util.stream.Collectors;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;

public class VendorServiceImpl implements VendorService{

    public final VendorMapper vendorMapper;
    public final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorMapper vendorMapper, VendorRepository vendorRepository) {
        this.vendorMapper = vendorMapper;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<VendorDTO> getAllVendors() {

        return vendorRepository.findAll()
            .stream()
            .map(vendor -> {
                VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + vendor.getId());
                return vendorDTO;
             })
            .collect(Collectors.toList());
    }

    @Override
    public VendorDTO getVendorById(Long id) {

        return vendorRepository.findById(id)
            .map(vendorMapper::vendorToVendorDTO)
            .map(vendorDTO -> {
                //set API URL
                vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + id);
                return vendorDTO;
             }).orElseThrow(ResourceNotFoundException::new);
    }

    public VendorDTO saveAndReturnDto(Vendor vendor) {

        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + savedVendor.getId());
        return vendorDTO;

    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        return saveAndReturnDto(vendorMapper.vendorDtoToVendor(vendorDTO));
    }

    @Override
    public VendorDTO saveVendorByDto(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);
        vendor.setId(id);
        return saveAndReturnDto(vendor);
    }

    @Override
    public VendorDTO patchVendorByDto(Long id, VendorDTO vendorDTO) {        
        return vendorRepository.findById(id).map( vendor -> {

            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }

            return saveAndReturnDto(vendor);

        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }
    
}
