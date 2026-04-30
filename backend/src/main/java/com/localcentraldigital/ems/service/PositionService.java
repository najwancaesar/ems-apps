package com.localcentraldigital.ems.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.localcentraldigital.ems.dto.PositionRequest;
import com.localcentraldigital.ems.dto.PositionResponse;
import com.localcentraldigital.ems.exception.DuplicateResourceException;
import com.localcentraldigital.ems.model.Position;
import com.localcentraldigital.ems.repository.PositionRepository;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<PositionResponse> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PositionResponse createPosition(PositionRequest request) {
        String name = request.getName().trim();
        if (positionRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Position name already exists");
        }

        Position position = new Position();
        position.setName(name);
        position.setDescription(request.getDescription());

        Position savedPosition = positionRepository.save(position);
        return toResponse(savedPosition);
    }

    private PositionResponse toResponse(Position position) {
        return new PositionResponse(
                position.getId(),
                position.getName(),
                position.getDescription(),
                position.getCreatedAt(),
                position.getUpdatedAt()
        );
    }
}
