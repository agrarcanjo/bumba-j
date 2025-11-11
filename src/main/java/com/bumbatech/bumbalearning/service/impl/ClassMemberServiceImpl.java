package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.ClassMember;
import com.bumbatech.bumbalearning.repository.ClassMemberRepository;
import com.bumbatech.bumbalearning.service.ClassMemberService;
import com.bumbatech.bumbalearning.service.dto.ClassMemberDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassMemberMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.ClassMember}.
 */
@Service
@Transactional
public class ClassMemberServiceImpl implements ClassMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassMemberServiceImpl.class);

    private final ClassMemberRepository classMemberRepository;

    private final ClassMemberMapper classMemberMapper;

    public ClassMemberServiceImpl(ClassMemberRepository classMemberRepository, ClassMemberMapper classMemberMapper) {
        this.classMemberRepository = classMemberRepository;
        this.classMemberMapper = classMemberMapper;
    }

    @Override
    public ClassMemberDTO save(ClassMemberDTO classMemberDTO) {
        LOG.debug("Request to save ClassMember : {}", classMemberDTO);
        ClassMember classMember = classMemberMapper.toEntity(classMemberDTO);
        classMember = classMemberRepository.save(classMember);
        return classMemberMapper.toDto(classMember);
    }

    @Override
    public ClassMemberDTO update(ClassMemberDTO classMemberDTO) {
        LOG.debug("Request to update ClassMember : {}", classMemberDTO);
        ClassMember classMember = classMemberMapper.toEntity(classMemberDTO);
        classMember = classMemberRepository.save(classMember);
        return classMemberMapper.toDto(classMember);
    }

    @Override
    public Optional<ClassMemberDTO> partialUpdate(ClassMemberDTO classMemberDTO) {
        LOG.debug("Request to partially update ClassMember : {}", classMemberDTO);

        return classMemberRepository
            .findById(classMemberDTO.getId())
            .map(existingClassMember -> {
                classMemberMapper.partialUpdate(existingClassMember, classMemberDTO);

                return existingClassMember;
            })
            .map(classMemberRepository::save)
            .map(classMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassMemberDTO> findAll() {
        LOG.debug("Request to get all ClassMembers");
        return classMemberRepository.findAll().stream().map(classMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ClassMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classMemberRepository.findAllWithEagerRelationships(pageable).map(classMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassMemberDTO> findOne(Long id) {
        LOG.debug("Request to get ClassMember : {}", id);
        return classMemberRepository.findOneWithEagerRelationships(id).map(classMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ClassMember : {}", id);
        classMemberRepository.deleteById(id);
    }
}
