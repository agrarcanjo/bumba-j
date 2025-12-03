package com.bumbatech.bumbalearning.web.rest.admin;

import com.bumbatech.bumbalearning.domain.Authority;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.AuthorityRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.AuthoritiesConstants;
import com.bumbatech.bumbalearning.service.UserService;
import com.bumbatech.bumbalearning.service.dto.AdminUserDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminUserResource {

    private static final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public AdminUserResource(UserService userService, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        log.debug("REST request to get all Users for admin");

        var users = userRepository.findAll();
        var userDTOs = users.stream().map(this::convertToAdminUserDTO).collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);

        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return ResponseEntity.ok(convertToAdminUserDTO(user));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<AdminUserDTO> updateUserRoles(@PathVariable Long id, @Valid @RequestBody Set<String> roles) {
        log.debug("REST request to update User roles : {}, {}", id, roles);

        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var authorities = roles
            .stream()
            .map(role ->
                authorityRepository
                    .findById(role)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authority not found: " + role))
            )
            .collect(Collectors.toSet());

        user.setAuthorities(authorities);
        user = userRepository.save(user);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(convertToAdminUserDTO(user));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<AdminUserDTO> activateUser(@PathVariable Long id) {
        log.debug("REST request to activate User : {}", id);

        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setActivated(true);
        user = userRepository.save(user);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(convertToAdminUserDTO(user));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AdminUserDTO> deactivateUser(@PathVariable Long id) {
        log.debug("REST request to deactivate User : {}", id);

        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setActivated(false);
        user = userRepository.save(user);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(convertToAdminUserDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);

        var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getLogin().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete admin user");
        }

        userRepository.delete(user);

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private AdminUserDTO convertToAdminUserDTO(User user) {
        var dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setActivated(user.isActivated());
        dto.setLangKey(user.getLangKey());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastModifiedBy(user.getLastModifiedBy());
        dto.setLastModifiedDate(user.getLastModifiedDate());
        dto.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return dto;
    }
}
