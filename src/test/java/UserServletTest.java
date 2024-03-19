import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;
import com.example.demo6.service.UserService;
import com.example.demo6.servlet.UserServlet;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServletTest {

    @InjectMocks
    private UserServlet servlet;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    User user;
    UserDto userRequest;
    UserDto userResponse;
    @Mock
    PrintWriter writer;
    StringWriter stringWriter;

    @BeforeEach
    public void init() {
        user = new User(1, "Anna", "Bogdanova", "Manager");
        userRequest = new UserDto(1, "Anna", "Bogdanova", "Manager");
        userResponse = new UserDto(1, "Anna", "Bogdanova", "Manager");
        stringWriter = new StringWriter();
    }

    @Test
    public void testDoGetWithId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        when(userService.getById(1)).thenReturn(user);

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetWithNullId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<User> userResponses = Arrays.asList(user, user);
        when(userService.getAll()).thenReturn(userResponses);

        List<String> expectedList = Stream.of(user, user)
                .map(authorResponse -> new Gson().toJson(authorResponse))
                .toList();


        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(userService, times(1)).getAll();
        assertEquals(expectedList.size(), userResponses.size());
    }

    @Test
    public void testDoPost() throws IOException, SQLException {
        when(request.getParameter("username")).thenReturn("Anna");
        when(request.getParameter("usersurname")).thenReturn("Bogdanova");
        when(request.getParameter("role")).thenReturn("Manager");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(userService, times(1)).save(UserDto.builder()
                .userName("Anna")
                .userSurname("Bogdanova")
                .role("Manager").
                build());
        verify(writer).print("Успешно добавлено");
        verify(writer).flush();
    }

    @Test
    public void testDoPost_InvalidData() throws IOException {
        when(request.getParameter("username")).thenReturn("Anna");
        when(request.getParameter("usersurname")).thenReturn("Bogdanova");
        when(request.getParameter("role")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).print("Невалидные данные");
        verify(writer).flush();
    }

    @Test
    public void testDoPut() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("Anna");
        when(request.getParameter("usersurname")).thenReturn("Bogdanova");
        when(request.getParameter("role")).thenReturn("Manager");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).print("Успешно обновлено");
        verify(writer).flush();
    }

    @Test
    public void testDoPut_InvalidIdFormat() throws IOException {
        when(request.getParameter("id")).thenReturn("invalid_id");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).println("Невалидный формат id");
    }
    @Test
    public void testDoDeleteWithValidId() throws SQLException, IOException {
        String id = "1";
        when(request.getParameter("id")).thenReturn(id);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

        verify(response.getWriter()).println("Успешно удалено");
        verify(userService).delete(Integer.valueOf(id));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(userService).delete(Integer.valueOf(id));
    }

    @Test
    public void testDoDeleteWithEmptyId() throws IOException, SQLException {
        when(request.getParameter("id")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

        verify(response.getWriter()).println("Пустой параметр id");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(userService, never()).delete(anyInt());
    }

    @Test
    public void testDoDeleteWithInvalidIdFormat() throws IOException, SQLException {
        String invalidId = "abc";
        when(request.getParameter("id")).thenReturn(invalidId);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

        verify(response.getWriter()).println("Невалидный формат id");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(userService, never()).delete(anyInt());
    }
}