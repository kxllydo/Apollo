## Get All Vehicles
curl -X GET http://localhost:8080/vehicle

## Post a valid Vehicle
curl -X POST http://localhost:8080/vehicle -H "Content-Type: application/json" -d "{\"vin\": \"abcd\", \"manufacturerName\": \"Toyota\", \"description\": \"highlander\", \"horsePower\": 5, \"modelName\": \"highlander\", \"modelYear\": 2014, \"purchasePrice\": 12000.12, \"fuelType\": \"gas\"}"

## Post an invalid Vehicle
curl -X POST http://localhost:8080/vehicle -H "Content-Type: application/json" -d "{\"vin\": \"abcd\", \"manufacturerName\": \"Toyota\", \"description\": \"highlander\", \"horsePower\": 5, \"modelName\": \"highlander\", \"moelYear\": 2014, \"purchasePrice\": 12000.12, \"fuelType\": \"gas\"}"

## Post a duplicate Vin
curl -X POST http://localhost:8080/vehicle -H "Content-Type: application/json" -d "{\"vin\": \"abcd\", \"manufacturerName\": \"Toyota\", \"description\": \"highlander\", \"horsePower\": 5, \"modelName\": \"highlander\", \"modelYear\": 2014, \"purchasePrice\": 12000.12, \"fuelType\": \"gas\"}"

## Get vehicle by vin
curl -X GET http://localhost:8080/vehicle/abcd

## Update a vehicle
curl -X PUT http://localhost:8080/vehicle/abcd -H "Content-Type: application/json" -d "{\"vin\": \"abcd\", \"manufacturerName\": \"Honda\", \"description\": \"Updated description - Certified pre-owned\", \"horsePower\": 158, \"modelName\": \"Accord\", \"modelYear\": 2021, \"purchasePrice\": 24500.00, \"fuelType\": \"Gasoline\"}"

## Update a nonexistent vehicle
curl -X PUT http://localhost:8080/vehicle/ab -H "Content-Type: application/json" -d "{\"vin\": \"abcd\", \"manufacturerName\": \"Honda\", \"description\": \"Updated description - Certified pre-owned\", \"horsePower\": 158, \"modelName\": \"Accord\", \"modelYear\": 2021, \"purchasePrice\": 24500.00, \"fuelType\": \"Gasoline\"}"

## Delete a vehicle
curl -X DELETE http://localhost:8080/vehicle/abcd