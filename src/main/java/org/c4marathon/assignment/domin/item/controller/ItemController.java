package org.c4marathon.assignment.domin.item.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.item.dto.ItemRequestDTO;
import org.c4marathon.assignment.domin.item.entity.Item;
import org.c4marathon.assignment.domin.item.service.ItemService;
import org.c4marathon.assignment.domin.user.controller.UserErrorStatus;
import org.c4marathon.assignment.domin.user.entity.Role;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.exception.GeneralException;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.domin.item.dto.ItemRequestDTO.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/register")
    public ApiPayload<?> registerItem(@RequestBody ItemRegisterDTO itemRegisterDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user.getRole() == Role.BUYER) {
            throw new GeneralException(ItemErrorStatus.INVALID_REGISTER);
        }

        itemService.register(itemRegisterDTO, user);
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }
}
