## VehicleController
* API for clients to create, update, or delete vehicle data

## VehicleDAO
* Responsible for handling all sql queries to the vehicle_db

## VehicleControllerTest
* Uses mockito to mock the VehicleDAO information and calls to the VehicleController

## Application Steps
1. Make sure you have jdk-21 installed to adhere to Mockito
2. mvn clean install -DskipTests
3. mvn spring-boot:run

# Vehicle API Documentation

Base URL: `/vehicle`

---

## GET /vehicle
Returns a list of all vehicles in the system.
Response: 200 OK, List<Vehicle>

## POST /vehicle
Creates a new vehicle using the JSON payload provided.
Response: 201 Created, Vehicle

### Request Body
Must be valid JSON and pass all `@Valid` validation rules. Must include all fields

#### Example
{
"vin": "12345",
"manufaturer_name": "Toyota",
"description": "Corolla",
"horse_power": 20,
"model_name": "Corolla",
"model_year": 2015,
"purchase_price": 1200.50
"fuel_type": "gas"
}

## GET /vehicle/{vin}
Gets vehicle based on vin
Response: 200 OK, Vehicle

## PUT /vehicle/{vin}
Updates vehicle based on vin
Response: 200 OK, Vehicle

## DELETE /vehicle/{vin}
Deletes vehicle based on vin
Response: 204 No Content

For all endpoints, 400 Bad Request will be returned for any parsing error. 422 Unprocessable Entity will be returns for any null or malformed attributes
## VehicleController
* API for clients to create, update, or delete vehicle data

## VehicleDAO
* Responsible for handling all sql queries to the vehicle_db

## VehicleControllerTest
* Uses mockito to mock the VehicleDAO information and calls to the VehicleController

## Application Steps
1. Make sure you have jdk-21 installed to adhere to Mockito
2. mvn clean install -DskipTests
3. mvn spring-boot:run

# Vehicle API Documentation

Base URL: `/vehicle`

---

## GET /vehicle
Returns a list of all vehicles in the system.
Response: 200 OK, List<Vehicle>

## POST /vehicle
Creates a new vehicle using the JSON payload provided.
Response: 201 Created, Vehicle

### Request Body
Must be valid JSON and pass all `@Valid` validation rules. Must include all fields

#### Example
{
"vin": "12345",
"manufaturer_name": "Toyota",
"description": "Corolla",
"horse_power": 20,
"model_name": "Corolla",
"model_year": 2015,
"purchase_price": 1200.50
"fuel_type": "gas"
}

## GET /vehicle/{vin}
Gets vehicle based on vin
Response: 200 OK, Vehicle

## PUT /vehicle/{vin}
Updates vehicle based on vin
Response: 200 OK, Vehicle

## DELETE /vehicle/{vin}
Deletes vehicle based on vin
Response: 204 No Content

For all endpoints, 400 Bad Request will be returned for any parsing error. 422 Unprocessable Entity will be returns for any null or malformed attributes
