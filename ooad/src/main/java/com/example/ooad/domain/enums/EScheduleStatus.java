package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EScheduleStatus {
    AVAILABLE,
    ON_LEAVE,
    CANCELLED;

    @JsonCreator
    public static EScheduleStatus fromString(String key) {
        if (key == null)
            return AVAILABLE;
        String normalized = key.trim().toUpperCase().replaceAll("\\s+", "_");
        try {
            return EScheduleStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            // Fallbacks for common variants
            String lower = key.trim().toLowerCase();
            if (lower.contains("active") || lower.contains("available") || lower.contains("hoạt"))
                return AVAILABLE;
            if (lower.contains("leave") || lower.contains("nghỉ"))
                return ON_LEAVE;
            if (lower.contains("cancel") || lower.contains("huy") || lower.contains("hủy"))
                return CANCELLED;
            // Default fallback
            return AVAILABLE;
        }
    }
}
