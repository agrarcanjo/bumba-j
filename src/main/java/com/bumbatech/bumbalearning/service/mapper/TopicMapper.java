package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring")
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {}
