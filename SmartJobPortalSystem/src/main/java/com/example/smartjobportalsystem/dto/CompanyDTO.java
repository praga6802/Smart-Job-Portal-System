package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Company;
import lombok.Data;

@Data
public class CompanyDTO {

    private Integer companyId;
    private String companyName;
    private String description;
    private String email;
    private String contact;
    private String url;
    private String type;
    private String size;
    private String location;
    private String gst;

    public CompanyDTO(Company company){
        this.companyId=company.getCompanyId();
        this.companyName=company.getName();
        this.description=company.getDescription();
        this.email=company.getEmail();
        this.contact=company.getContact();
        this.url=company.getUrl();
        this.type=company.getType();
        this.size=company.getSize();
        this.location=company.getLocation();
        this.gst=company.getGst();
    }
}
