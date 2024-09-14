# fx-rates-widget



## Getting started

## How To Build and Deploy via Terminal?
1. Open terminal and goto project directory
2. On the root project directory type "mvn clean package"
3. Generated Jar file will be inside target folder
4. Type "java -jar target/exam-0.0.1-SNAPSHOT.jar"


## How To Build and Deploy via IntelijIDEA?
1. Open project and select Run(Green Play) button located on the upper right corner of the IDE

## How to test?
1. Run the project using one of the above method
2. Go to http://localhost:8080/swagger-ui.html

or

1. Open postman
2. Send POST request via http://localhost:8080/exchangerates/convert
Header: application/json
Request Body: 
```
{
    "buyCurrency": "USD",
    "sellCurrency": "PHP",
    "buyAmount": 125,
    "sellAmount": 0
}
```
