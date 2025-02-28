package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public static CommentResponse mapToCommentResponse(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment is null");
        }
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment mapToComment(CommentCreateRequest commentDto, User user, Item item) {
        if (commentDto == null || user == null || item == null) {
            throw new IllegalArgumentException("One of parameters is null");
        }
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }
}