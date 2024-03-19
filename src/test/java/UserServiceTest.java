import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;
import com.example.demo6.mapper.UserMapper;
import com.example.demo6.repository.UserRepository;
import com.example.demo6.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper mapper;
    @InjectMocks
    UserServiceImpl userService;

    User user;
    UserDto userDto;

    @BeforeEach
    public void init() {
        user = new User(1, "Anna", "Bogdanova", "Manager");
        userDto = new UserDto(1, "Anna", "Bogdanova", "Manager");
    }

    @Test
    public void testSaveUser() throws SQLException {
        doNothing().when(userRepository).save(user);
        when(mapper.toEntity(userDto)).thenReturn(user);

        userService.save(userDto);

        verify(userRepository).save(user);
    }

    @Test
    public void save_shouldThrowException() throws SQLException {
        assertThrows(Exception.class, () -> doThrow().when(userRepository).save(user));
        userService.save(userDto);
    }

    @Test
    public void testGetById() throws SQLException {
        Integer id = 1;
        when(userRepository.findById(id)).thenReturn(user);
        when(mapper.toDtoUser(user)).thenReturn(user);

        User response = userService.getById(id);

        verify(userRepository, times(1)).findById(id);
        verify(mapper, times(1)).toDtoUser(user);
        assertNotNull(response);
    }

    @Test
    void getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);

        List<UserDto> expectedResponses = new ArrayList<>();
        expectedResponses.add(userDto);
        expectedResponses.add(userDto);

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toDtoUser(user)).thenReturn(user);

        List<User> actualResponses = userService.getAll();

        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i), actualResponses.get(i));
        }

        verify(mapper, times(users.size())).toDtoUser(any(User.class));
    }

    @Test
    public void update() throws SQLException {
        doNothing().when(userRepository).update(user);

        when(mapper.toEntity(userDto)).thenReturn(user);

        userService.update(userDto);

        verify(userRepository, times(1)).update(user);
    }

    @Test
    void delete() throws SQLException {
        Integer id = 1;
        doNothing().when(userRepository).delete(id);

        userService.delete(id);

        verify(userRepository, times(1)).delete(id);
    }
}
