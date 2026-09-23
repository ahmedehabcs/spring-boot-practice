package com.project.project.campaign.dto;

import java.math.BigDecimal;

public record CampaignResponse(
        Long campaignId,
        String campaignName,
        String description,
        BigDecimal price
) {
}
