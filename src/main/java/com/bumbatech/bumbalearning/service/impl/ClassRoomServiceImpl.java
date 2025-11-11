package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.service.ClassRoomService;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassRoomMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.ClassRoom}.
 */
@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassRoomServiceImpl.class);

    private final ClassRoomRepository classRoomRepository;

    private final ClassRoomMapper classRoomMapper;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository, ClassRoomMapper classRoomMapper) {
        this.classRoomRepository = classRoomRepository;
        this.classRoomMapper = classRoomMapper;
    }

    @Override
    public ClassRoomDTO save(ClassRoomDTO classRoomDTO) {
        LOG.debug("Request to save ClassRoom : {}", classRoomDTO);
        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDTO);
        classRoom = classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }

    @Override
    public ClassRoomDTO update(ClassRoomDTO classRoomDTO) {
        LOG.debug("Request to update ClassRoom : {}", classRoomDTO);
        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDTO);
        classRoom = classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }

    @Override
    public Optional<ClassRoomDTO> partialUpdate(ClassRoomDTO classRoomDTO) {
        LOG.debug("Request to partially update ClassRoom : {}", classRoomDTO);

        return classRoomRepository
            .findById(classRoomDTO.getId())
            .map(existingClassRoom -> {
                classRoomMapper.partialUpdate(existingClassRoom, classRoomDTO);

                return existingClassRoom;
            })
            .map(classRoomRepository::save)
            .map(classRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassRoomDTO> findOne(Long id) {
        LOG.debug("Request to get ClassRoom : {}", id);
        return classRoomRepository.findById(id).map(classRoomMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ClassRoom : {}", id);
        classRoomRepository.deleteById(id);
    }
}
