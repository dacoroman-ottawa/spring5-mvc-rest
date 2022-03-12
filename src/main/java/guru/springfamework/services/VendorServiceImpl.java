package guru.springfamework.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService{

    public final VendorMapper vendorMapper;
    public final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorMapper vendorMapper, VendorRepository vendorRepository) {
        this.vendorMapper = vendorMapper;
        this.vendorRepository = vendorRepository;
    }

    private String getVendorUrl(Long id) {
        return VendorController.BASE_URL + "/" + id;
    }

    @Override
    public VendorListDTO getAllVendors() {
        List<VendorDTO> vendorDTOS = vendorRepository
                .findAll()
                .stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendorUrl(getVendorUrl(vendor.getId()));
                    return vendorDTO;
                })
                .collect(Collectors.toList());

        return new VendorListDTO(vendorDTOS);
    }

    @Override
    public VendorDTO getVendorById(Long id) {

        return vendorRepository.findById(id)
            .map(vendorMapper::vendorToVendorDTO)
            .map(vendorDTO -> {
                //set API URL
                vendorDTO.setVendorUrl(getVendorUrl(id));
                return vendorDTO;
             }).orElseThrow(ResourceNotFoundException::new);
    }

    public VendorDTO saveAndReturnDto(Vendor vendor) {

        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        vendorDTO.setVendorUrl(getVendorUrl(savedVendor.getId()));
        return vendorDTO;

    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        return saveAndReturnDto(vendorMapper.vendorDtoToVendor(vendorDTO));
    }

    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);
        vendor.setId(id);
        return saveAndReturnDto(vendor);
    }

    @Override
    public VendorDTO patchVendorByDTO(Long id, VendorDTO vendorDTO) {        
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
