package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {

    @Test
    public void whenGetIndexPage() {
        var indexController = new IndexController();
        var view = indexController.getIndex();

        assertThat(view).isEqualTo("index");
    }
}