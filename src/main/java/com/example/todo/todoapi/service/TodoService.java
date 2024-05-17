package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    public TodoListResponseDTO create(TodoCreateRequestDTO requestDTO) throws Exception {
        todoRepository.save(requestDTO.toEntity());
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());
        return retrieve();
    }
    
    // 할 일 목록 가져오기
    public TodoListResponseDTO retrieve() throws Exception {
        List<Todo> entityList = todoRepository.findAll();
        
        List<TodoDetailResponseDTO> dtoList = entityList.stream()
//                .map(entity -> new TodoDetailResponseDTO(entity))
                .map(TodoDetailResponseDTO::new)
                .toList();
        
        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }
    
    public TodoListResponseDTO delete(final String todoId) throws Exception {
        
        todoRepository.findById(todoId).orElseThrow(
                () -> {
                    log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}", todoId);
                    throw new RuntimeException("id가 존재하지 않아 삭제에 실패했습니다.");
                }
        );
        todoRepository.deleteById(todoId);
        return retrieve();
    }
    
    public TodoListResponseDTO update(final TodoModifyRequestDTO requestDTO) throws Exception {
        
        Optional<Todo> targetEntity = todoRepository.findById(requestDTO.getId());
        
        targetEntity.ifPresent(todo -> {
            todo.setDone(requestDTO.isDone());
            
            todoRepository.save(todo);
        });
        
        return retrieve();
    }
}
