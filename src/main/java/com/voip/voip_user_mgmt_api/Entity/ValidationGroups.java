package com.voip.voip_user_mgmt_api.Entity;

import jakarta.validation.groups.Default;

// Validation groups for different scenarios
public interface ValidationGroups {
    interface Create extends Default {}
    interface Update extends Default {}
    interface PartialUpdate extends Default {}
}
