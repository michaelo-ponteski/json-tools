# json-tools ![example workflow](https://github.com/michaelo-ponteski/json-tools/actions/workflows/ci.yml/badge.svg)

Application for programmers who need to reformat or filter data structures saved in JSON format and to compare the structures with each other. JSON tools application allows you to both minify the unminified JSON representation, as well as the reverse operation

## Features

### Core Operations

1. **Minify JSON**: Removes whitespace and formatting to create compact JSON
2. **Prettify JSON**: Formats JSON with proper indentation and spacing
3. **Filter Keys**: Keeps only specified keys in the JSON object
4. **Exclude Keys**: Removes specified keys from the JSON object
5. **Compare JSON**: Compares two JSON strings and returns differences

## Getting Started

### Installation

#### Download Pre-built JAR

1. Go to the [GitHub repository releases page](https://github.com/michaelo-ponteski/json-tools/releases)
2. Download the latest `json-tools.jar`
3. Run the application:

```bash
java -jar json-tools.jar
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

### 1. Minify JSON

Compresses JSON by removing unnecessary whitespace.

**Endpoint**: `POST /api/json/minify`

**cURL Example**:

```bash
curl --location 'http://localhost:8080/api/json/minify' \
--header 'Content-Type: application/json' \
--data '{
  "Error" : "Invalid HTTP Method",
  "ErrorCode" : "405",
  "ErrorDesc" : "Method Not Allowed"
}'
```

### 2. Prettify JSON

Formats JSON with proper indentation and spacing.

**Endpoint**: `POST /api/json/prettify`

**cURL Example**:

```bash
curl --location 'http://localhost:8080/api/json/prettify' \
--header 'Content-Type: application/json' \
--data '{"Error":"Invalid HTTP Method","ErrorCode":"405","ErrorDesc":"Method Not Allowed"}'
```

### 3. Filter Keys

Keeps only the specified keys in the JSON object.

**Endpoint**: `POST /api/json/filter`

**Parameters**:

- `json` (String): The JSON string to filter
- `keys` (List<String>): Array of keys to keep

**cURL Example**:

```bash
curl --location 'http://localhost:8080/api/json/filter?keys=Error' \
--header 'Content-Type: application/json' \
--data '{"Error":"Invalid HTTP Method","ErrorCode":"405","ErrorDesc":"Method Not Allowed"}'
```

### 4. Exclude Keys

Removes specified keys from the JSON object.

**Endpoint**: `POST /api/json/exclude`

**Parameters**:

- `json` (String): The JSON string to process
- `keys` (List<String>): Array of keys to exclude

**cURL Example**:

```bash
curl --location 'http://localhost:8080/api/json/exclude?keys=Error' \
--header 'Content-Type: application/json' \
--data '{"Error":"Invalid HTTP Method","ErrorCode":"405","ErrorDesc":"Method Not Allowed"}'
```

### 5. Compare JSON

Compares two JSON strings and returns the differences.

**Endpoint**: `POST /api/json/compare`

**Parameters**:

- `json1` (String): First JSON string
- `json2` (String): Second JSON string

**cURL Example**:

```bash
curl --location 'http://localhost:8080/api/json/compare' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'json1={"Error":"Invalid HTTP Method","ErrorCode":"405","ErrorDesc":"Method Not Allowed"}' \
--data-urlencode 'json2={
  "Error" : "Invalid HTTP Method",
  "ErrorCode" : "404",
  "ErrorDesc" : "Method Not Allowed"
}'
```

### Basic JSON Processing

1. **Minifying a JSON object**:

   - Send a formatted JSON string to the minify endpoint
   - Receive a compressed version without whitespace

2. **Prettifying a minified JSON**:
   - Send a compact JSON string to the prettify endpoint
   - Receive a well-formatted version with proper indentation

### Advanced Filtering

1. **Keeping specific fields**:

   - Use the filter endpoint with a list of keys you want to retain
   - All other keys will be removed from the result

2. **Removing sensitive data**:
   - Use the exclude endpoint with keys containing sensitive information
   - The specified keys will be removed while keeping everything else

### JSON Comparison

1. **Finding differences**:
   - Send two JSON strings to the compare endpoint
   - Receive a detailed report of all differences between the objects

## Changelog

### Version 0.1

- Initial release with core JSON processing features
- Decorator pattern implementation
- REST API endpoints
- Comprehensive error handling
