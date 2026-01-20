package com.kevin.aquaconnect;
public class ServiceRequest {
    private String requestId;
    private String userName; // Ensure this is named 'userName' to match your Activity
    private String serviceType;
    private String description;
    private String address;
    private String urgency;
    private String status;

    // 1. Empty constructor (Required for Firebase)
    public ServiceRequest() {}

    // 2. ONLY ONE Multi-parameter constructor
    public ServiceRequest(String requestId, String userName, String serviceType, String description, String address, String urgency, String status) {
        this.requestId = requestId;
        this.userName = userName;
        this.serviceType = serviceType;
        this.description = description;
        this.address = address;
        this.urgency = urgency;
        this.status = status;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}