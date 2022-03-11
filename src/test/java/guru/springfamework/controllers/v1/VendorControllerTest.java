package guru.springfamework.controllers.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.services.ResourceNotFoundException;
import guru.springfamework.services.VendorService;

public class VendorControllerTest  extends AbstractRestControllerTest {

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                    .setControllerAdvice(new RestResponseEntityExceptionHandler())
                    .build();
    }

    @Test
    public void testListVendors() throws Exception {

        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Lululemon");
        vendor1.setVendorUrl(VendorController.BASE_URL + "/1");

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setName("Dacia");
        vendor2.setVendorUrl(VendorController.BASE_URL + "/2");

        when(vendorService.getAllVendors()).thenReturn(Arrays.asList(vendor1, vendor2));

        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void testGetVendorById() throws Exception {

        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Lululemon");
        vendor1.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.getVendorById(anyLong())).thenReturn(vendor1);

        //when
        mockMvc.perform(get(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Lululemon")));
    }

    @Test
    public void createNewVendor() throws Exception {
        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Lululemon");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor1.getName());
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.createNewVendor(vendor1)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(post(VendorController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(vendor1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Lululemon")))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/1")));
    }

    @Test
    public void testUpdateVendor() throws Exception {
        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Lululemon");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor1.getName());
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.saveVendorByDto(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Lululemon")))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/1")));
    }

    @Test
    public void testPatchVendor() throws Exception {

        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Lululemon");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor1.getName());
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.patchVendorByDto(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Lululemon")))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/1")));
    }


    @Test
    public void testDeleteVendor() throws Exception {

        mockMvc.perform(delete(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).deleteVendorById(anyLong());
    }


    @Test
    public void testNotFoundException() throws Exception {

        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(VendorController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
}
