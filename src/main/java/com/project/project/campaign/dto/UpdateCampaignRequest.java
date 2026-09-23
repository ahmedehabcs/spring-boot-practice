package com.project.project.campaign.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateCampaignRequest(
        String campaignName,
        String description,

        @Positive
        BigDecimal price,

        Set<Long> clientIds,
        Set<Long> employeeIds
) {
}
