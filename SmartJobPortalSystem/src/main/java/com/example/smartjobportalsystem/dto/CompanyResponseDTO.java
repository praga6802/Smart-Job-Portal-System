package com.example.smartjobportalsystem.dto;

import com.example.smartjobportalsystem.entity.Company;
import lombok.Data;

@Data
public class CompanyResponseDTO {
        private String name;
        private String description;
        private String email;
        private String contact;
        private String url;
        private String type;
        private String size;
        private String location;

        public CompanyResponseDTO(Company company) {
            this.description = company.getDescription();
            this.email = company.getEmail();
            this.contact = company.getContact();
            this.url = company.getUrl();
            this.type = company.getType();
            this.size = company.getSize();
            this.location = company.getLocation();
            this.name=company.getName();
        }

}
