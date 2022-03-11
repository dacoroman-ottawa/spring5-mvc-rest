package guru.springfamework.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.services.VendorService;

@Controller
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<VendorListDTO> getListofVendors(){

        return new ResponseEntity<VendorListDTO>(new VendorListDTO(vendorService.getAllVendors()),
                HttpStatus.OK);

    }

    @GetMapping({"/{id}"})
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long id){
        return new ResponseEntity<VendorDTO>(vendorService.getVendorById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VendorDTO> createNewCustomer(@RequestBody VendorDTO vendorDTO){
        return new ResponseEntity<VendorDTO>(vendorService.createNewVendor(vendorDTO),
                HttpStatus.CREATED);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<VendorDTO> updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO){
        return new ResponseEntity<VendorDTO>(vendorService.saveVendorByDto(id, vendorDTO),
                HttpStatus.OK);
    }

    @PatchMapping({"/{id}"})
    public ResponseEntity<VendorDTO> patchVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO){
        return new ResponseEntity<VendorDTO>(vendorService.patchVendorByDto(id, vendorDTO),
                HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id){

        vendorService.deleteVendorById(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
}
