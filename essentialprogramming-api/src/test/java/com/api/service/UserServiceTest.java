package com.api.service;


import com.api.entities.User;
import com.api.model.UserInput;
import com.api.output.UserJSON;
import com.api.repository.UserRepository;
import com.email.service.EmailManager;
import com.internationalization.EmailMessages;
import com.internationalization.Messages;
import com.spring.ApplicationContextFactory;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import lombok.val;
import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.util.test.AssertJUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;
import java.time.Clock;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final SoftAssertions softAssertions = new SoftAssertions();

    private static MockedStatic<ApplicationContextFactory> applicationContextFactoryMock;
    private static MockedStatic<Messages> messagesMock;
    private static MockedStatic<EmailMessages> emailMessagesMock;

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailManager emailManager;

    @Spy
    private final Clock clock = Clock.systemUTC();


    @BeforeAll
    static void init() {
        applicationContextFactoryMock = Mockito.mockStatic(ApplicationContextFactory.class);
        messagesMock = Mockito.mockStatic(Messages.class);
        emailMessagesMock = Mockito.mockStatic(EmailMessages.class);
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    public void afterTest() {
        reset(userRepository);
    }


    @Test
    void save_method_has_transactional_annotation() throws NoSuchMethodException {
        val method = UserService.class.getDeclaredMethod("save", UserInput.class, Language.class);
        assertThat(method).is(annotatedWith(Transactional.class));
    }

    @Test
    void load_method_throws_exception_if_user_does_not_exist() {

        //given
        when(userRepository.findByEmail("notExistingEmail@email.com")).thenReturn(Optional.empty());

        applicationContextFactoryMock.when(() -> ApplicationContextFactory.getBean("messageSource", ReloadableResourceBundleMessageSource.class)).thenReturn(any());
        messagesMock.when(() -> Messages.get(any(), Language.ENGLISH)).thenReturn("Your account doesn't exist or access is forbidden");


        //when
        Throwable throwable = catchThrowable(() -> userService.loadUser("notExistingEmail@email.com", Language.ENGLISH));

        //then
        assertThrowsAPIException(() -> userService.loadUser("notExistingEmail@email.com", Language.ENGLISH));
        assertThat(throwable).isInstanceOf(ApiException.class);
        assertThrows(ApiException.class, () -> userService.loadUser("notExistingEmail@email.com", Language.ENGLISH));
    }

    @Test
    void load_user_successfully() {
        //given
        final User user = createUser();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        //when
        UserJSON userJSON = userService.loadUser(any(), any());

        //then
        assertThat(userService).is(instanceOf(UserService.class));

        assertEquals(user.getFirstName(), userJSON.getFirstName());
        assertEquals(user.getLastName(), userJSON.getLastName());

    }

    @Test
    void save_user_successfully() throws GeneralSecurityException {
        //given
        final UserInput userInput = createUserInput();
        final User user = createUser();

        doReturn(user).when(userRepository).save(any());
        doNothing().when(emailManager).send(any(), any(), any(), any(), any());

        doCallRealMethod().when(userService).save(any(), any());


        //when
        UserJSON userJSON = userService.save(userInput, Language.ENGLISH);

        //then
        verify(userService).save(eq(userInput), eq(Language.ENGLISH));
        verify(userRepository).save(any());

        assertEquals(userInput.getFirstName(), userJSON.getFirstName());
        assertEquals(userInput.getLastName(), userJSON.getLastName());

        assertThat(userInput.getFirstName()).isEqualTo("John");
    }

    @Test
    void check_email_available() {
        //given
        final User user = createUser();

        doAnswer(invocation -> {
            user.setEmail((String) invocation.getArguments()[0]);
            return Optional.of(user);
        }).when(userRepository).findByEmail("john@email.com");


        //when
        boolean isEmailAvailable = userService.checkAvailabilityByEmail("john@email.com");

        //then
        verify(userRepository).findByEmail(any());
        softAssertions.assertThat(isEmailAvailable).isEqualTo(false);

        assertThat(isEmailAvailable).satisfies(new Condition<>(expectedValue -> expectedValue.equals(false), ""));
        assertThat(isEmailAvailable).is(notAvailable());
        assertThat(isEmailAvailable).isFalse();

    }

    private static <T> Condition<T> notAvailable() {
        return new Condition<>(expectedValue -> expectedValue.equals(false), " ");
    }


    private static User createUser() {
        final User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Smith");
        return user;
    }

    private static UserInput createUserInput() {
        final UserInput user = new UserInput();
        user.setFirstName("John");
        user.setLastName("Smith");
        return user;
    }

}
