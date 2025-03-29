package com.example.gestortareas.respositories;

import com.example.gestortareas.data.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select r.* from roles r inner join user_roles ur on ur.role_id = r.id and ur.user_id = :userId", nativeQuery = true)
    List<Role> findByUserId(@Param("userId") Long userId);
}
