package com.project.project.email.dto;

import java.util.Map;

public record EmailMessage(
        String to,
        String subject,
        String template,
        Map<String, Object> variables
) {
}
