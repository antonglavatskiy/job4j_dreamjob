package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private HttpServletRequest request;

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        request = new MockHttpServletRequest();
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPage() {
        var view = userController.getRegistationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRedirectVacanciesPage() {
        var user = new User(1, "email", "name", "pass");
        when(userService.save(any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(user, model);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenRegisterUserThenGetErrorPageWithMessage() {
        var user = new User(1, "email", "name", "pass");
        var expectedException = new RuntimeException("Пользователь с такой почтой уже существует");
        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenGetLoginPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenRedirectVacanciesPage() {
        var user = new User(1, "email", "name", "pass");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenLogoutUserThenRedirectLoginPage() {
        var view = userController.logout(request.getSession());

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}