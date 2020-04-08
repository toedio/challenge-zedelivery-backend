# Backend Challenge
This is my challenge, I'm Victor Silva and applicant to Software Engineer position.

Contact: toedio6@gmail.com / 11 99656-6750

## Technologies
- Java 8
- Spring Boot 2.2.6
- Maven
- MongoDB

## Services
### 1. Create partner
POST http://localhost:8080/partners

Body example
```
{
    "tradingName": "Teste Victor 2",
    "ownerName": "Victor 2",
    "document": "96.672.286/0001-20",
    "coverageArea": {
        "type": "MultiPolygon",
        "coordinates": [
            [
                [
                    [
                        -23.62617,
                        -46.55169
                    ],
                    [
                        -23.62746,
                        -46.55327
                    ],
                    [
                        -23.62812,
                        -46.55063
                    ],
                    [
                        -23.62686,
                        -46.54987
                    ],
                    [
                        -23.62617,
                        -46.55169
                    ]
                ]
            ],
            [
                [
                    [
                        -23.62691,
                        -46.55149
                    ],
                    [
                        -23.62755,
                        -46.55104
                    ],
                    [
                        -23.6274,
                        -46.55079
                    ],
                    [
                        -23.62691,
                        -46.55149
                    ]
                ]
            ]
        ]
    },
    "address": {
        "type": "Point",
        "coordinates": [
            -24.62671,
            -47.55111
        ]
    }
}
```

### 2. Get partner by id
GET http://localhost:8080/partners/2

Note: When the application starts the partners are populated in the MongoDB collection. This not happen when run with "prd" profile.

### 3. Search partner by location
GET http://localhost:8080/partners/search?lat=-23.45558&lng=-45.7852
    
## Build
Run command ```mvn clean install```

Note: This command execute the tests

## Run Locally

The system uses MongoDB and to start it is necessary the local server is running, and application will automatically connect using the url http://localhost:27017. If you don't have it installed go to https://docs.mongodb.com/manual/tutorial/manage-mongodb-processes/

When MongoDB local server is running, run command ```mvn spring-boot-run``` and the 
API will be exposed at port 8080

## Deploy
Project has a dockerfile, you can deploy in all cloud platforms

Run command ```docker build . -t backendchallenge``` to build docker image and run ```docker run -t backendchallenge``` to run

If you want to run on a specific port, run ```docker run -p YOUR_PORT:8080 -t backendchallenge```

Note: The production database credentials need to be changed in the application-prd.properties file. Otherwise, the application will not start