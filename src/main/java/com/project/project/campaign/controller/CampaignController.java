package com.project.project.campaign.controller;

import com.project.project.campaign.dto.CampaignResponse;
import com.project.project.campaign.dto.CreateCampaignRequest;
import com.project.project.campaign.dto.UpdateCampaignRequest;
import com.project.project.campaign.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping
    public List<CampaignResponse> getAll(){
        return campaignService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public CampaignResponse getCampaignById(@PathVariable Long id){
        return campaignService.getCampaignById(id);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PostMapping
    public CampaignResponse createCampaign(@Valid @RequestBody CreateCampaignRequest campaign){
        return campaignService.createCampaign(campaign);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PatchMapping("/{id}")
    public CampaignResponse updateCampaign(@PathVariable Long id ,@Valid @RequestBody UpdateCampaignRequest updateCampaign){
        return campaignService.updateCampaign(id, updateCampaign);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @DeleteMapping("/{id}")
    public String deleteCampaign(@PathVariable Long id){
        return campaignService.deleteCampaign(id);
    }
}
