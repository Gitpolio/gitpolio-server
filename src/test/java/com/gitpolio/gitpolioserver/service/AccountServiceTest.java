package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.domain.IdType;
import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.LoginInfoDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.entity.Account;
import com.gitpolio.gitpolioserver.exception.LoginFailureException;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import com.gitpolio.gitpolioserver.jwt.AuthTokenUtils;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private LoremIpsum lorem;

    @BeforeEach
    public void init() {
        accountRepository = mock(AccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        accountService = new AccountService(accountRepository, passwordEncoder);
        lorem = LoremIpsum.getInstance();
    }

    /*
    회원가입시 다음과 같은 정보를 필요로 한다
    - 이름
    - 이메일
    - 암호
    - Github 토큰
    기존 유저와 이메일이 중복되지 않은경우, 암호를 Hashing 한뒤 DB 에 회원가입 정보로 얻은 유저 정보를 저장한다

    따라서, register method 는 RegisterInformation(name, email, password, token) 을 인자로 받고
    AccountDto(name, email, encoded password, token) 를 반환한다
     */@Test
    public void testRegister() {
         //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfoDto();

        //이메일 중복여부 확인과 계정 저장이 제대로 되었는지 확인하기 위해서 account repository 를 mocking 한다
        when(accountRepository.existsByEmail(registerInfo.getEmail())).thenReturn(false);
        //~003 테스트 대상 검사 (when - Answer 로 검사하기떄문에 테스트 대상 실행 이전에 setting 한다)
        when(accountRepository.save(any())).thenAnswer(invocation -> {
                    Account account = invocation.getArgument(0);

                    assertEquals(registerInfo.getName(), account.getName());
                    assertEquals(registerInfo.getEmail(), account.getEmail());
                    //encode 된 password 를 저장해야한다
                    assertEquals(passwordEncoder.encode(registerInfo.getPassword()), account.getEncodedPassword());
                    assertEquals(registerInfo.getGithubToken(), account.getGithubToken());

                    return null;
                }
        );

        //비밀번호가 제대로 encoding 되었는지 확인하기 위해서 password encoder 의 encode method 를 mocking 한다
        String salt = lorem.getWords(1);
        when(passwordEncoder.encode(registerInfo.getPassword())).thenReturn(registerInfo.getPassword() + salt);

        //~002 테스트 대상 실행
        //AccountService 의 register method 를 호출한다
        AccountDto account = accountService.register(registerInfo);

        //~003 테스트 대상 검사
        //db 에 저장한 값을 return 하였는지 검사한다
        assertEquals(registerInfo.getName(), account.getName());
        assertEquals(registerInfo.getEmail(), account.getEmail());
        assertEquals(passwordEncoder.encode(registerInfo.getPassword()), account.getEncodedPassword());
        assertEquals(registerInfo.getGithubToken(), account.getGithubToken());
    }

    /*
    이미 같은 이메일로 가입된 이메일이 있을경우, LoginFailureException 을 반환해야한다.
     */@Test
    public void testRegisterFailure() {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfoDto();

        //이메일이 중복되어야 하므로 true 를 반환한다
        when(accountRepository.existsByEmail(registerInfo.getEmail())).thenReturn(true);

        //~002~003 테스트 대상 실행 및 테스트 대상 검사
        //테스트 중 RegisterFailureException 이 throwing 되는지 검사한다
        assertThrows(RegisterFailureException.class,
                () -> accountService.register(registerInfo));
    }

    /*
    로그인시 다음과 같은 정보를 필요로 한다
    - 아이디 (email or name)
    - 암호
    기존 유저와 이메일이 중복되지 않은경우, 암호를 Hashing 한뒤 DB 에 회원가입 정보로 얻은 유저 정보를 저장한다

    로그인과정에서 서버와 클라이언트의 통신은 다음과 같다
    - 클라이언트가 서버에게 로그인 요청을 보낸다 (이때, 사용자로부터 입력받은 로그인 정보를 body 에 담아 보낸다)
    - 서버에서 로그인 정보를 검증한다
    - 검증이 완료되면 검증 결과를 기반으로 auth_token 을발급한다
    클라이언트가 신빙성이 있으므로 authorization_code 는 제외한다

    따라서, login method 는 LoginInfo(id, password) 를 인자로 받고, auth token 문자열을 반환한다
     */@Test //Id 의 type 이 name 일 경우
    public void testLoginByName() {
        testLogin(IdType.NAME, (loginInfo, account) -> {
            //Id 의 type 이 name 이므로 existByName 으로 id 를 찾을 시 true 를 반환해야한다.
            when(accountRepository.existsByName(loginInfo.getId())).thenReturn(true);
            when(accountRepository.existsByEmail(loginInfo.getId())).thenReturn(false);

            when(accountRepository.getByName(loginInfo.getId())).thenReturn(account);
        });
    }

    @Test //Id 의 type 이 email 일 경우
    public void testLoginByEmail() {
        testLogin(IdType.EMAIL, (loginInfo, account) -> {
            //Id 의 type 이 email 이므로 existByEmail 으로 id 를 찾을 시 true 를 반환해야한다.
            when(accountRepository.existsByName(loginInfo.getId())).thenReturn(false);
            when(accountRepository.existsByEmail(loginInfo.getId())).thenReturn(true);

            when(accountRepository.getByEmail(loginInfo.getId())).thenReturn(account);
        });
    }

    @Test //Id 를 찾을 수 없을경우 (id 가 name 도 아니고, email 도 아닐경우)
    public void testLoginFailureBecauseWrongId() {
        assertThrows(LoginFailureException.class, () ->
                testLogin(IdType.NAME, (loginInfo, account) -> {
                    //Id 의 type 이 name 이므로 existByName 으로 id 를 찾을 시 true 를 반환해야한다.
                    when(accountRepository.existsByName(loginInfo.getId())).thenReturn(false);
                    when(accountRepository.existsByEmail(loginInfo.getId())).thenReturn(false);
                }));
    }

    @Test //Password 가 맞지 않을경우
    public void testLoginFailureBecauseWrongPassword() {
         assertThrows(LoginFailureException.class, ()-> {
             //~001 테스트 환경 구성
             //Random data 가 담긴 RegisterInfoDto 를 가져온다
             LoginInfoDto loginInfo = getRandomLoginInfoDto();
             //accountRepository 에 account 요청시 반환할 account 이다
             Account account = mock(Account.class);

             when(accountRepository.existsByName(loginInfo.getId())).thenReturn(false);
             when(accountRepository.existsByEmail(loginInfo.getId())).thenReturn(true);

             when(accountRepository.getByEmail(loginInfo.getId())).thenReturn(account);

             when(passwordEncoder.matches(any(), any())).thenReturn(false);

             //~002 테스트 대상 실행
             accountService.login(loginInfo);
         });
    }

    private void testLogin(IdType loginIdType, LoginStrategy loginStrategy) {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        LoginInfoDto loginInfo = getRandomLoginInfoDto(loginIdType);
        //accountRepository 에 account 요청시 반환할 account 이다
        Account account = mock(Account.class);

        //login 의 password 검사 단계에서 repository 를 통해 불러올 account 의 password 를 설정한다
        // 이때, password 는 encoding 된 상태이므로 이를 구현하고자 salt 를 추가한다.
        String salt = lorem.getWords(1);
        String rawPassword = loginInfo.getRawPassword();
        String encodedPassword = rawPassword + salt;
        when(account.getEncodedPassword()).thenReturn(encodedPassword);

        //ID가 email 일 경우인지 name 일 경우인지 결정
        loginStrategy.loginBy(loginInfo, account);

        //로그인이 성공하려면 encode 된 password 와 law password 가 match 되어야 한다
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        //~002 테스트 대상 실행
        String authToken = accountService.login(loginInfo);
        String tokenId = AuthTokenUtils.getAccountIdFromToken(authToken);
        assertEquals(tokenId, loginInfo.getId());
    }

    @FunctionalInterface
    private interface LoginStrategy {
         void loginBy(LoginInfoDto loginInfoDto, Account account);
    }

    //Random data 로 구성된 LoginInfoDto 를 생성하여 반환한다
    private LoginInfoDto getRandomLoginInfoDto() {
         IdType[] idTypes = IdType.values();
         return getRandomLoginInfoDto(idTypes[new Random().nextInt(idTypes.length)]);
    }

    //Random data 로 구성된 LoginInfoDto 를 생성하여 반환한다
    private LoginInfoDto getRandomLoginInfoDto(IdType idType) {
         String id = "";
         switch (idType) {
             case NAME:
                 id = lorem.getName();
                 break;
             case EMAIL:
                 id = lorem.getEmail();
                 break;
         }
         String password = lorem.getWords(1);
         return new LoginInfoDto(id, password);
    }

    //Random data 로 구성된 RegisterInfoDto 를 생성하여 반환한다
    public RegisterInfoDto getRandomRegisterInfoDto() {
         String name = lorem.getName();
         String email = lorem.getEmail();
         String password = lorem.getWords(1);
         String githubToken = new String(Base64Coder.encode(lorem.getWords(4).getBytes(StandardCharsets.UTF_8)));

        return new RegisterInfoDto(name, email, password, githubToken);
    }
}