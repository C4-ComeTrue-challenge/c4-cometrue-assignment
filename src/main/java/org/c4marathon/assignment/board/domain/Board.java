package org.c4marathon.assignment.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.global.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String name;

    @Builder
    private Board(String name) {
        this.name = name;
    }

    public static Board create(String name) {
        return Board.builder()
                .name(name)
                .build();
    }

    public void changeBoardName(String name) {
        this.name = name;
    }
}
