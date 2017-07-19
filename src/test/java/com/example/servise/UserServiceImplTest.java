package com.example.servise;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.UserServiceImpl;
import com.example.util.exception.NotFoundException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Spy
	@InjectMocks
	private UserServiceImpl sut = new UserServiceImpl();

	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private AuthService authServiceMock;

	@Mock
	private User dummyUser;

	@Test
	public void testGet() throws Exception {
		int anyId = 1;
		Mockito.when(userRepositoryMock.findById(anyId)).thenReturn(dummyUser);

		User actual = sut.findById(anyId);

		Mockito.verify(userRepositoryMock).findById(anyId);
		assertSame(dummyUser, actual);
	}

	@Test
	public void testGetNotFound() throws Exception {
		int anyId = 1000;
		Mockito.when(userRepositoryMock.findById(anyId)).thenReturn(null);

		expectedException.expect(NotFoundException.class);
		expectedException.expectMessage(String.format("User with id=%d is not found", anyId));

		sut.findById(anyId);
		;
	}

	@Test
	public void testGetByLogin() throws Exception {
		String dummyLogin = "anyLogin";
		Mockito.when(userRepositoryMock.findByLogin(dummyLogin)).thenReturn(dummyUser);

		User actual = sut.findUserByLogin(dummyLogin);

		Mockito.verify(userRepositoryMock).findByLogin(dummyLogin);
		assertSame(dummyUser, actual);
	}

	@Test
	public void testGetByLoginNotFound() throws Exception {
		String dummyLogin = "anyLogin";
		Mockito.when(userRepositoryMock.findByLogin(dummyLogin)).thenReturn(null);

		expectedException.expect(NotFoundException.class);
		expectedException.expectMessage(String.format("User with login=%s is not found", dummyLogin));

		sut.findUserByLogin(dummyLogin);
	}

	@Test
	public void testSave() throws Exception {
		Mockito.when(userRepositoryMock.save(dummyUser)).thenReturn(dummyUser);

		sut.saveUser(dummyUser);
		User actualUser = sut.findById(dummyUser.getId());
		Mockito.verify(userRepositoryMock).save(dummyUser);
		assertSame(dummyUser, actualUser);
	}

}