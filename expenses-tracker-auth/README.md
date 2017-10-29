## Generate key

```
keytool -genkeypair -alias jwt -keyalg RSA -dname "CN=jwt, L=Lugano, S=Lugano, C=CH" 
    -keypass mySecretKey -keystore jwt.jks -storepass mySecretKey
```

## Useful materials

* [Securing with private OAuth server](http://www.swisspush.org/security/2016/10/17/oauth2-in-depth-introduction-for-enterprises)
* [Spring OAuth Guide](https://spring.io/guides/tutorials/spring-boot-oauth2/#_social_login_authserver)