# Backend Challenge
This is my challenge, I'm Victor Silva and applicant to Software Engineer position.

Contact: toedio6@gmail.com / 11 99656-6750

## Technologies
- Java 8
- Spring Boot 2.2.6
- Maven
- MongoDB
- Redis

## Technical Decisions
### MongoDB
I used mongodb to save and get partners because it have GeoJSON support and we can easily query using "$near" and "$geoIntersects" operators. It too have caching that improves queries perform.
### Cache with Redis
I implement caching in get partner by id service. this improve performance when has a big volume of requests. For now, application is only caching this service but can add services and can implements other features like time to live (ttl), cache evict on delete and cache put on update. Redis was database chosen to save cache because its very performatic for that and frequently used as cache database.
### Validations in controller
PartnerController class has validations' annotation like @NotNull @Min, @Max, @Valid. I chose this way because is a better way validate in first application's layer avoiding processing invalid objetcs. PartnerDTO class has annotations too like @NotBlank, @CNPJ, @Null and this annotations is validates by @Valid annotation in PartnerController class.
### Converters
Application has a converter service. It is used to convert DTO into an entity and vice versa and this service is used in controller. This way PartnerService know only domain and PartnerController know only DTO.
## Services
API was documented using swagger.io and file swagger-doc.json is available on the project. The services are also described below.
### 1. Create partner
POST /partners

Example (body)
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
GET /partners/{id}

Example ```GET /partners/1```

### 3. Search partner by location
GET /partners?lat={lat}&lng={lng}

Example ```GET /partners?lat=-23.89415&lng=-46.858412```
    
## Run Tests
Run command ```mvn test```

## Run Locally

The system uses MongoDB and to start it is necessary the local server is running, and application will automatically connect using the url http://localhost:27017. If you don't have it installed go to https://docs.mongodb.com/manual/tutorial/manage-mongodb-processes/

When MongoDB local server is running, run command ```mvn spring-boot-run``` and the 
API will be exposed at port 8080

## Deploy
Project has a dockerfile, you can deploy in all cloud platforms

Run command ```docker build . -t backendchallenge``` to build docker image and run ```docker run -t backendchallenge``` to run

If you want to run on a specific port, run ```docker run -p YOUR_PORT:8080 -t backendchallenge```

Note: The production database credentials need to be changed in the application-prd.properties file. Otherwise, the application will not start