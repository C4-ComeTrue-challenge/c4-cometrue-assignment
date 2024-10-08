package org.c4marathon.assignment.domin.item.repository;

import org.c4marathon.assignment.domin.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
