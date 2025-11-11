package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.ClassMember} entity.
 */
@Schema(description = "ClassMember - Alunos matriculados na turma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Data de matrícula", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant enrolledAt;

    @NotNull
    private ClassRoomDTO classRoom;

    @NotNull
    private UserDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Instant enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public ClassRoomDTO getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoomDTO classRoom) {
        this.classRoom = classRoom;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassMemberDTO)) {
            return false;
        }

        ClassMemberDTO classMemberDTO = (ClassMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassMemberDTO{" +
            "id=" + getId() +
            ", enrolledAt='" + getEnrolledAt() + "'" +
            ", classRoom=" + getClassRoom() +
            ", student=" + getStudent() +
            "}";
    }
}
