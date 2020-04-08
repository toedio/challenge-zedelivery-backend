package delivery.ze.challenge.controller;

import delivery.ze.challenge.dto.PartnerDTO;
import delivery.ze.challenge.service.PartnerService;
import delivery.ze.challenge.validator.groups.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/partners")
@Validated
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @GetMapping("/{id}")
    @Validated
    public PartnerDTO findById(@PathVariable @NotBlank String id) { return partnerService.getById(id); }

    @PostMapping
    @Validated(Create.class)
    public PartnerDTO create(@RequestBody @NotNull(groups = Create.class) @Valid PartnerDTO partnerDTO) {
        return partnerService.create(partnerDTO);
    }

    @GetMapping(value = "/search", params = {"lat", "lng"})
    public PartnerDTO searchByLatLng(@RequestParam @NotNull @Min(-90) @Max(90) Double lat,
                                     @RequestParam @NotNull @Min(-180) @Max(180) Double lng) {
        return partnerService.search(lat, lng);
    }
}
