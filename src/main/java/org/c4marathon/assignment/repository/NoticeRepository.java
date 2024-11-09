package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
