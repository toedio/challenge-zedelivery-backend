package delivery.ze.challenge.controller;

import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.dto.PartnerDTO;
import delivery.ze.challenge.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
@RequiredArgsConstructor
public class PartnerController {

    private final ConversionService conversionService;

    private final PartnerService partnerService;

    @GetMapping("/{id}")
    @Validated
    public PartnerDTO findById(@PathVariable @NotBlank String id) {
        Partner partner = partnerService.getById(id);
        return conversionService.convert(partner, PartnerDTO.class);
    }

    @PostMapping
    @Validated
    public PartnerDTO create(@RequestBody @NotNull @Valid PartnerDTO partnerDTO) {
        final Partner partner = conversionService.convert(partnerDTO, Partner.class);
        final Partner savedPartner = partnerService.create(partner);
        return conversionService.convert(savedPartner, PartnerDTO.class);
    }

    @GetMapping(params = {"lat", "lng"})
    public PartnerDTO searchByLatLng(@RequestParam @NotNull @Min(-180) @Max(180) Double lng,
                                     @RequestParam @NotNull @Min(-90) @Max(90) Double lat) {
        final Partner partner = partnerService.search(lng, lat);
        return conversionService.convert(partner, PartnerDTO.class);
    }
}
