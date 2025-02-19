package com.example.springmvcwithjsonview;


import com.example.springmvcwithjsonview.Entity.Order;
import com.example.springmvcwithjsonview.Entity.OrderProduct;
import com.example.springmvcwithjsonview.Entity.UserEntity;
import com.example.springmvcwithjsonview.ExceptionsHandler.UserNotFoundException;
import com.example.springmvcwithjsonview.Repository.UserRepository;
import com.example.springmvcwithjsonview.Service.UserService;
import com.example.springmvcwithjsonview.View.UserDetails;
import com.example.springmvcwithjsonview.View.UserSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private UserEntity userEntity;
    private List<UserEntity> users;
    private List<Order> orders;
    private List<OrderProduct> products;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        products = new ArrayList<>();
        orders.add(new Order(1L, new BigDecimal(100), "New", userEntity, products));
        userEntity = getUserEntity();
        users = new ArrayList<>();
        users.add(userEntity);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAll_shouldFindAll() {
        when(userRepository.findAll()).thenReturn(users);
        List<UserEntity> usersTest = userService.findAll();
        assertEquals(users.size(), usersTest.size());
        assertEquals(userEntity, usersTest.get(0));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_shouldThrowWhenUserNotFound() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findAll());
        assertEquals("No users found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_shouldFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));

        UserEntity found = userService.findById(1L);

        assertEquals(userEntity, found);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_shouldThrowWhenIdIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(null));
        assertEquals("Id can't be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository, never()).findById(any());
    }

    @Test
    public void testUpdate_shouldUpdate() {
        when(userRepository.updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail())).thenReturn(1);
        when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));

        UserEntity updatedUserEntity = userService.update(userEntity);

        assertNotNull(updatedUserEntity);
        assertEquals(userEntity.getUserId(), updatedUserEntity.getUserId());
        assertEquals(userEntity.getFullName(), updatedUserEntity.getFullName());
        assertEquals(userEntity.getEmail(), updatedUserEntity.getEmail());

        verify(userRepository, times(1))
                .updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail());
        verify(userRepository, times(1)).findById(userEntity.getUserId());
    }

    @Test
    public void testUpdate_shouldThrowWhenUserNotFound() {
        when(userRepository.updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail()))
                .thenReturn(0);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.update(userEntity));

        assertEquals("user not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1))
                .updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail());
        verify(userRepository, never()).findById(any());
    }

    @Test
    public void testUpdate_shouldThrowWhenUserEntityIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.update(null));

        assertEquals("user can't be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository, never()).updateUser(any(), any(), any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    public void testUpdate_shouldThrowWhenUserNotFoundAfterUpdate() {
        when(userRepository.updateUser(userEntity.getUserId(),
                userEntity.getFullName(), userEntity.getEmail())).thenReturn(1);
        when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.update(userEntity));

        assertEquals("user not found after update", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1))
                .updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail());
        verify(userRepository, times(1)).findById(userEntity.getUserId());
    }

    @Test
    public void testSave_shouldSave() {
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity savedUserEntity = userService.save(userEntity);

        assertNotNull(savedUserEntity);
        assertEquals(userEntity.getUserId(), savedUserEntity.getUserId());
        assertEquals(userEntity.getFullName(), savedUserEntity.getFullName());
        assertEquals(userEntity.getEmail(), savedUserEntity.getEmail());

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testSave_shouldThrowWhenUserEntityIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.save(null));

        assertEquals("user can't be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSave_shouldThrowWhenUserNotSaved() {
        when(userRepository.save(userEntity)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.save(userEntity));

        assertEquals("user not saved", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testDelete_shouldDelete() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.deleteUserEntityByUserId(1L)).thenReturn(1);

        UserEntity deletedUserEntity = userService.delete(1L);

        assertNotNull(deletedUserEntity);
        assertEquals(userEntity.getUserId(), deletedUserEntity.getUserId());
        assertEquals(userEntity.getFullName(), deletedUserEntity.getFullName());
        assertEquals(userEntity.getEmail(), deletedUserEntity.getEmail());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteUserEntityByUserId(1L);
    }

    @Test
    public void testDelete_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(1L));

        assertEquals("user not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteUserEntityByUserId(any());
    }

    @Test
    public void testDelete_shouldThrowWhenIdIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(null));

        assertEquals("id can't be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).deleteUserEntityByUserId(any());
    }

    @Test
    public void testDelete_shouldThrowWhenUserNotDeleted() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.deleteUserEntityByUserId(1L)).thenReturn(0);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(1L));

        assertEquals("user not deleted", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteUserEntityByUserId(1L);
    }

    @Test
    void jsonView_UserSummary_includesUserIdFullNameEmail() throws Exception {
        String json = objectMapper.writerWithView(UserSummary.class).writeValueAsString(userEntity);

        assertTrue(json.contains("\"userId\":1"));
        assertTrue(json.contains("\"fullName\":\"Bob\""));
        assertTrue(json.contains("\"email\":\"bob@example.com\""));
    }

    @Test
    void jsonView_UserDetails_includesUserIdFullNameEmailOrders() throws Exception {
        String json = objectMapper.writerWithView(UserDetails.class).writeValueAsString(userEntity);
        System.out.println(json);
        assertTrue(json.contains("\"userId\":1"));
        assertTrue(json.contains("\"fullName\":\"Bob\""));
        assertTrue(json.contains("\"email\":\"bob@example.com\""));
        assertTrue(json.contains("\"orders\":[{\"id\":1,\"orderPrice\":100,\"status\":\"New\",\"user\":null,\"orderProducts\":[]}]"));
    }
    @Test
    void jsonView_UserSummary_excludesOrders() throws Exception {
        String json = objectMapper.writerWithView(UserSummary.class).writeValueAsString(userEntity);
        assertFalse(json.contains("orders"));
    }

    UserEntity getUserEntity() {
        return UserEntity.builder()
                .userId(1L)
                .fullName("Bob")
                .email("bob@example.com")
                .orders(orders)
                .build();
    }
}
