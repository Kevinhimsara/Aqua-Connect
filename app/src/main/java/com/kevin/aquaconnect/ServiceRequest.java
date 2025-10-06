package com.kevin.aquaconnect;

public class ServiceRequest {
    private String requestId;
    private String serviceType;
    private String description;
    private String address;
    private String urgency;

    // Empty constructor required for Firebase
    public ServiceRequest() {}

    public ServiceRequest(String requestId, String serviceType, String description, String address, String urgency) {
        this.requestId = requestId;
        this.serviceType = serviceType;
        this.description = description;
        this.address = address;
        this.urgency = urgency;
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
}
