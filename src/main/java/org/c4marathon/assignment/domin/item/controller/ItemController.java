package org.c4marathon.assignment.domin.item.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.item.service.ItemService;
import org.c4marathon.assignment.domin.user.entity.Role;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.exception.GeneralException;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.c4marathon.assignment.domin.item.dto.ItemRequestDTO.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiPayload<?> registerItem(@RequestBody ItemRegisterDTO itemRegisterDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user.getRole() == Role.BUYER) {
            throw new GeneralException(ItemErrorStatus.INVALID_REGISTER);
        }

        itemService.register(itemRegisterDTO, user);
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }
}
