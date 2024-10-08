package org.c4marathon.assignment.domin.item.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.item.dto.ItemRequestDTO;
import org.c4marathon.assignment.domin.item.entity.Item;
import org.c4marathon.assignment.domin.item.repository.ItemRepository;
import org.c4marathon.assignment.domin.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    @Transactional
    public void register(ItemRequestDTO.ItemRegisterDTO itemRegisterDTO, User user) {
        Item item = Item.builder()
                .user(user)
                .name(itemRegisterDTO.getName())
                .price(itemRegisterDTO.getPrice())
                .stock(itemRegisterDTO.getStock())
                .description(itemRegisterDTO.getDescription())
                .build();

        itemRepository.save(item);
    }
}
