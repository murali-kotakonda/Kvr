package com.kvr.invoice.service;

import com.kvr.invoice.model.Client;
import com.kvr.invoice.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    
    public List<Client> getAllActiveClients() {
        return clientRepository.findByIsDeletedFalse();
    }
    
    public List<Client> searchClients(String search) {
        if (search == null || search.trim().isEmpty()) {
            return getAllActiveClients();
        }
        return clientRepository.searchAllFields(search);
    }
    
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Client saveClient(Client client) {
        if (client.getGstin() != null && !client.getGstin().isEmpty()) {
            clientRepository.findByGstin(client.getGstin()).ifPresent(c -> {
                if (client.getId() == null || !c.getId().equals(client.getId())) {
                    throw new RuntimeException("GSTIN already exists");
                }
            });
        }
        return clientRepository.save(client);
    }
    
    @Transactional
    public void softDeleteClient(Long id) {
        clientRepository.findById(id).ifPresent(client -> {
            client.setIsDeleted(true);
            clientRepository.save(client);
        });
    }
}
