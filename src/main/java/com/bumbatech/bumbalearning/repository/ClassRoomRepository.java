package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.ClassRoom;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClassRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long>, JpaSpecificationExecutor<ClassRoom> {
    @Query("select classRoom from ClassRoom classRoom where classRoom.teacher.login = ?#{authentication.name}")
    List<ClassRoom> findByTeacherIsCurrentUser();
}
