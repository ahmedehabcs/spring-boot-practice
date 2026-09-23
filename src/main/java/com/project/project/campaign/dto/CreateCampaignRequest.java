package com.project.project.campaign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record CreateCampaignRequest(
        @NotBlank
        String campaignName,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        Set<Long> clientIds,
        Set<Long> employeeIds
) {
}
