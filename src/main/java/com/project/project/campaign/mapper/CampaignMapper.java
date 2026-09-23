package com.project.project.campaign.mapper;

import com.project.project.campaign.dto.CampaignResponse;
import com.project.project.campaign.dto.CreateCampaignRequest;
import com.project.project.campaign.entity.Campaign;

public class CampaignMapper {

    public static Campaign toEntity(CreateCampaignRequest createCampaignRequest) {
        Campaign campaign = new Campaign();

        campaign.setCampaignName(createCampaignRequest.campaignName());
        campaign.setCampaignDescription(createCampaignRequest.description());
        campaign.setPrice(createCampaignRequest.price());

        return campaign;
    }

    public static CampaignResponse toResponse(Campaign campaign) {
        return new CampaignResponse(
                campaign.getId(),
                campaign.getCampaignName(),
                campaign.getCampaignDescription(),
                campaign.getPrice()
        );
    }
}
