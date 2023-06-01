package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileDto fileDto;

    private FileController fileController;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileDto = mock(FileDto.class);
        fileController = new FileController(fileService);
    }

    @Test
    public void whenGetByIdThenOk() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.of(fileDto));
        var responseEntity = fileController.getById(1);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenGetByIdThenNotFound() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.empty());
        var responseEntity = fileController.getById(1);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}