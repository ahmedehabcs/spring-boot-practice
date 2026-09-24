package com.project.project.campaign.service;

import com.project.project.campaign.dto.CampaignResponse;
import com.project.project.campaign.dto.CreateCampaignRequest;
import com.project.project.campaign.dto.UpdateCampaignRequest;
import com.project.project.campaign.entity.Campaign;
import com.project.project.campaign.mapper.CampaignMapper;
import com.project.project.campaign.repository.CampaignRepository;
import com.project.project.common.exception.ApiException;
import com.project.project.user.entity.Role;
import com.project.project.user.entity.User;
import com.project.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private UserRepository userRepository;


    public List<CampaignResponse> getAll() {
        return campaignRepository.findAll().stream().map(CampaignMapper::toResponse).toList();
    }

    public CampaignResponse getCampaignById(Long id) {
        return campaignRepository.findById(id).map(CampaignMapper::toResponse).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "campaign not found"));
    }

    public CampaignResponse createCampaign(CreateCampaignRequest request) {
        Campaign campaign = CampaignMapper.toEntity(request);

        // check clients
        if (request.clientIds() != null) {
            List<User> clients = userRepository.findAllById(request.clientIds());
            if (clients.size() != request.clientIds().size()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Client is not found");
            }
            for (User client : clients) {
                if (client.getRole() != Role.CLIENT) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, client.getName() + " is not a CLIENT");
                }
            }
            campaign.setClients(new HashSet<>(clients));
        }

        // check admin & employee
        if (request.employeeIds() != null) {
            List<User> employees = userRepository.findAllById(request.employeeIds());

            if (employees.size() != request.employeeIds().size()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Employee is not found");
            }

            for (User employee : employees) {
                if (employee.getRole() != Role.EMPLOYEE && employee.getRole() != Role.ADMIN) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, employee.getName() + " is not ADMIN or EMPLOYEE");
                }
            }
            campaign.setEmployees(new HashSet<>(employees));
        }

        Campaign savedCampaign = campaignRepository.save(campaign);
        return CampaignMapper.toResponse(savedCampaign);
    }

    public CampaignResponse updateCampaign(Long id, UpdateCampaignRequest request) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "campaign not found"));

        if (request.campaignName() != null) campaign.setCampaignName(request.campaignName());
        if (request.description() != null) campaign.setCampaignDescription(request.description());
        if (request.price() != null) campaign.setPrice(request.price());

        if (request.clientIds() != null) {
            List<User> client = userRepository.findAllById(request.clientIds());
            if (client.size() != request.clientIds().size())
                throw new ApiException(HttpStatus.BAD_REQUEST, "Client is not found");

            for (User clients : client) {
                if (clients.getRole() != Role.CLIENT)
                    throw new ApiException(HttpStatus.BAD_REQUEST, clients.getName() + " is not a CLIENT");
            }
            campaign.setClients(new HashSet<>(client));
        }

        if (request.employeeIds() != null) {
            List<User> employee = userRepository.findAllById(request.employeeIds());
            if (employee.size() != request.employeeIds().size())
                throw new ApiException(HttpStatus.BAD_REQUEST, "Employee is not found");

            for (User employees : employee) {
                if (employees.getRole() != Role.EMPLOYEE && employees.getRole() != Role.ADMIN)
                    throw new ApiException(HttpStatus.BAD_REQUEST, employees.getName() + " is not a ADMIN or Employee");
            }
            campaign.setEmployees(new HashSet<>(employee));
        }

        Campaign savedCampaign = campaignRepository.save(campaign);
        return CampaignMapper.toResponse(savedCampaign);
    }

    public String deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "campaign not found"));
        campaignRepository.delete(campaign);
        return "Campaign has been deleted";
    }

}
