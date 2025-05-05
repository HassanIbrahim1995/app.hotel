package com.shiftmanager.api.dto;

import java.time.LocalDateTime;

/**
 * DTO for notification
 */
public class NotificationDTO {
    /**
     * Builder class for NotificationDTO
     */
    public static class Builder {
        private final NotificationDTO dto;
        
        public Builder() {
            dto = new NotificationDTO();
        }
        
        public Builder id(Long id) {
            dto.setId(id);
            return this;
        }
        
        public Builder message(String message) {
            dto.setMessage(message);
            return this;
        }
        
        public Builder type(String type) {
            dto.setType(type);
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            dto.setCreatedAt(createdAt);
            return this;
        }
        
        public Builder read(boolean read) {
            dto.setRead(read);
            return this;
        }
        
        public Builder referenceId(Long referenceId) {
            dto.setReferenceId(referenceId);
            return this;
        }
        
        public Builder employeeId(Long employeeId) {
            dto.setEmployeeId(employeeId);
            return this;
        }
        
        public Builder readAt(LocalDateTime readAt) {
            dto.setReadAt(readAt);
            return this;
        }
        
        public NotificationDTO build() {
            return dto;
        }
    }
    
    /**
     * Create a new builder instance
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    private Long id;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private boolean read;
    private LocalDateTime readAt;
    private Long referenceId;
    private Long employeeId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public Long getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDateTime getReadAt() {
        return readAt;
    }
    
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
    
    /**
     * Create a copy of this DTO
     * @return A new instance with the same values
     */
    public NotificationDTO copy() {
        return builder()
            .id(this.id)
            .message(this.message)
            .type(this.type)
            .createdAt(this.createdAt)
            .read(this.read)
            .referenceId(this.referenceId)
            .employeeId(this.employeeId)
            .readAt(this.readAt)
            .build();
    }
}