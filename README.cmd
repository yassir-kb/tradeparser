# Tradeparser Assistant

This project contains a tradeparser assistant that was created by Yassir EL KOBI.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Build and Run

1. Clone this repository:

```bash
git clone https://github.com/yassir-kb/tradeparser.git
```

2. Open a terminal in the project directory and run:

```bash
mvn clean package
```

3. Run the generated JAR file:

```bash
java -jar target\tradeparser-0.0.1-SNAPSHOT.jar <path_to_email_file>
```

## Usage

The tradeparser assistant can be used to answer questions about code, such as providing suggestions for code snippets or files. The tradeparser assistant will then respond with a JSON object containing the answer, for example::


The code assistant will then respond with a JSON object containing the answer, for example:

```json
{
    "reference":"624916107",
    "tradeDate":"2021-08-30",
    "symbol":"EURUSD",
    "currency":"USD",
    "amount":78361.67,
    "amountCounterValue":65948.79,
    "rate":1.18822,
    "valueDate":"2021-10-30"
}
```

If the code assistant is unable to answer the question, it will return an error message:

```json
{
  "error": "Usage: bin/console app:email:parser <path_to_email_file>"
}
```
## The tradeparser assistant is 100% covered by tests.

Look at tradeparser/htmlReport/index.html for more details

## Merci ;)