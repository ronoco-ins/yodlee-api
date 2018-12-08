# Yodlee API by ronoco

## Building from Source
To build everying using Maven
```$xslt
mvn clean install
```
Note: This will run the unit tests which may fail if the test keys are inaccessible.  To build the source without the tests use the ``-DskipTests=true`` option.

### Properties
To use the API the following properties need to be set in your application properties or system properties.  The classes are designed to be used with Spring Framework.

| Property                    | Description                                                 |
| :---                        | :---                                                        |
| yodlee.api.accountPassword  | The password to use for the Yodlee Accounts on User Sign Up.|
| yodlee.api.url              | The URL to the Yodlee API                                   |
| yodlee.cobrand.login        | Cobrand Login                                               |
| yodlee.cobrand.name         | Cobrand Server Name                                         |
| yodlee.cobrand.locale       | Locale                                                      |
| yodlee.cobrand.password     | Cobrand Password                                            |
| yodlee.fastlink.url         | Fastlink URL to use Fastlink Service                        |

### Loading with Spring
To load with Spring add the following to a Configuration class or your SpringBootApplication class.
```$xslt
@ComponentScan("com.ronoco.yodlee")
```
This will bootstrap all the required components for the Yodlee service and provide access to the Fastlink controller.  The Fastlink controller can be found at
```$xslt
/yodlee/fastlink/finance
```
or
```$xslt
/yodlee/fastlink/insurance
```
depending on your application.  The paths are left relative to allow for SAME_ORIGIN iframe security to be applied.

## License
Yodlee API by ronoco is licensed under the terms of MIT License.