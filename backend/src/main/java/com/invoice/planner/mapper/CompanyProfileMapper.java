package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;

import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.entity.CompanyProfile;

@Component
public class CompanyProfileMapper {
	 
	public CompanyProfileResponse toResponse(CompanyProfile companyProfile) {
		if (companyProfile == null) {
			return null;
		}
		CompanyProfileResponse response = new CompanyProfileResponse();
		response.setTrackingId(companyProfile.getTrackingId());
		response.setCompanyName(companyProfile.getCompanyName());
		response.setAddress(companyProfile.getAddress());
		response.setCity(companyProfile.getCity());
		response.setPostalCode(companyProfile.getPostalCode());
		response.setCountry(companyProfile.getCountry());
		response.setPhoneNumber(companyProfile.getPhoneNumber());
		response.setEmail(companyProfile.getEmail());
		response.setWebsite(companyProfile.getWebsite());
		response.setTaxNumber(companyProfile.getTaxNumber());
		response.setRegistrationNumber(companyProfile.getRegistrationNumber());
		response.setHasLogo(companyProfile.getLogoPath() != null);
		response.setCreatedAt(companyProfile.getCreatedAt());
		response.setUpdatedAt(companyProfile.getUpdatedAt());
		
		return response;
	}
	
	public CompanyProfile toEntity(CompanyProfileResponse response) {
		CompanyProfile companyProfile = new CompanyProfile();
		companyProfile.setTrackingId(response.getTrackingId());
		companyProfile.setCompanyName(response.getCompanyName());
		companyProfile.setAddress(response.getAddress());
		companyProfile.setCity(response.getCity());
		companyProfile.setPostalCode(response.getPostalCode());
		companyProfile.setCountry(response.getCountry());
		companyProfile.setPhoneNumber(response.getPhoneNumber());
		companyProfile.setEmail(response.getEmail());
		companyProfile.setWebsite(response.getWebsite());
		companyProfile.setTaxNumber(response.getTaxNumber());
		companyProfile.setRegistrationNumber(response.getRegistrationNumber());
		
		return companyProfile;
	}
	
	public void updateEntity(CompanyProfile companyProfile, CompanyProfileResponse response) {
		companyProfile.setCompanyName(response.getCompanyName());
		companyProfile.setAddress(response.getAddress());
		companyProfile.setCity(response.getCity());
		companyProfile.setPostalCode(response.getPostalCode());
		companyProfile.setCountry(response.getCountry());
		companyProfile.setPhoneNumber(response.getPhoneNumber());
		companyProfile.setEmail(response.getEmail());
		companyProfile.setWebsite(response.getWebsite());
		companyProfile.setTaxNumber(response.getTaxNumber());
		companyProfile.setRegistrationNumber(response.getRegistrationNumber());
		
	}
}
