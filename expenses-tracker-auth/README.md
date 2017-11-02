## Generate key

```
keytool -genkeypair -alias jwt -keyalg RSA -dname "CN=jwt, L=Lugano, S=Lugano, C=CH" 
    -keypass mySecretKey -keystore jwt.jks -storepass mySecretKey
```


## Authentication process

* Browser accesses / and the request passes through the OAuth2ClientContextFilter and OAuth2ClientAuthenticationProcessingFilter as the context does not match. The configured context path for login is /googleLogin
* The security interceptor FilterSecurityInterceptor detects that the the user is anonymous and throws an access denied exception.
* Spring security's ExceptionTranslationFilter catches the access denied exception and asks the configured authentication entry point to handle it which issues a redirect to /googleLogin.
* For the request /googleLogin, the filter OAuth2AuthenticationProcessingFilter tries to access the Google protected resource and an UserRedirectRequiredException is thrown which is translated into a HTTP redirect to Google (with the OAuth2 details) by OAuth2ClientContextFilter.
* On successful authentication from Google the browser is redirected back to /googleLogin with the OAuth code. The filter OAuth2AuthenticationProcessingFilter handles this and creates an Authentication object and updates the SecurityContext.
* At this point the user is fully authenticated and redirect to / is issued by the OAuth2AuthenticationProcessingFilter.
* FilterSecurityInterceptor allows the request to proceed as the SecurityContext contains an Authentication object which is authenticated.
* Finally the application page which is secured using an expression like isFullyAuthenticated() or similar is rendered.

## Key classes

* org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter
* org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter

## Useful materials

* [Securing with private OAuth server](http://www.swisspush.org/security/2016/10/17/oauth2-in-depth-introduction-for-enterprises)
* [Spring OAuth Guide](https://spring.io/guides/tutorials/spring-boot-oauth2/#_social_login_authserver)
* [More implementation details](https://stackoverflow.com/questions/26056780/spring-security-oauth2-google-web-app-in-redirect-loop)
* [Logging in from SPA -> OAuth2 flow](https://dzone.com/articles/sso-oauth2-angular-js-and)