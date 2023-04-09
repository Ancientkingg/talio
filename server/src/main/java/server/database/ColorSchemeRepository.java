package server.database;

import commons.ColorScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorSchemeRepository extends JpaRepository<ColorScheme, Long> {
}
