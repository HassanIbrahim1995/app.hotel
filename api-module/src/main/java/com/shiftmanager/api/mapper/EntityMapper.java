package com.shiftmanager.api.mapper;

import java.util.List;
import org.mapstruct.MappingTarget;

/**
 * Generic interface for entity to DTO mapping and vice versa
 * @param <D> DTO type
 * @param <E> Entity type
 */
public interface EntityMapper<D, E> {
    
    /**
     * Convert entity to DTO
     * @param entity Entity to convert
     * @return Converted DTO
     */
    D toDto(E entity);
    
    /**
     * Convert DTO to entity
     * @param dto DTO to convert
     * @return Converted entity
     */
    E toEntity(D dto);
    
    /**
     * Update entity from DTO
     * @param dto DTO with updated values
     * @param entity Entity to update
     * @return Updated entity
     */
    E updateEntityFromDto(D dto, @MappingTarget E entity);
    
    /**
     * Convert list of entities to list of DTOs
     * @param entities Entities to convert
     * @return List of converted DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Convert list of DTOs to list of entities
     * @param dtos DTOs to convert
     * @return List of converted entities
     */
    default List<E> toEntityList(List<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
}
