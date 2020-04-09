# Backend Challenge
This is my challenge, I'm Victor Silva and applicant to Software Engineer position.

Contact: toedio6@gmail.com / 11 99656-6750

## Technologies
- Java 8
- Spring Boot 2.2.6
- Maven
- MongoDB
- Redis
- Docker

## Technical Decisions
### MongoDB
I used mongodb to save and get partners because it has GeoJSON support and we can easily query using "$near" and "$geoIntersects" operators. It too have caching that improves queries perform.

### Cache with Redis
I implement caching in get partner by id service. This improves performance when has a big volume of requests. For now, application is only caching this service but can be added services and can be implemented others features like time to live (ttl), cache evict on delete and cache put on update. Redis was database chosen to save cache because its very performatic for that and frequently used as cache database.

### Validations in controller
PartnerController class has validations' annotation like @NotNull @Min, @Max, @Valid. I chose this way because is a better way validate in first application's layer avoiding processing invalid objetcs. PartnerDTO class has annotations too like @NotBlank, @CNPJ, @Null and annotations is validates by @Valid annotation in PartnerController class.

### Converters
Application has a converter service. It is used to convert DTO into an entity and vice versa and this service is used in the PartnerController. This way PartnerService know only domain and PartnerController know only DTO.

### Docker Compose
The application use docker compose that automatically starts all necessary dependencies (redis and mongodb) and starts the web application on port 8080. I chose the docker compose because it is an easy tool to run the docker with multiple containers, platforms with multiple clouds and standardize the infrastructure.

## Services
API was documented using swagger.io and file swagger-doc.json is available on the project. The services are also described below.

### 1. Create partner:
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

### 2. Get partner by "id:
GET /partners/{id}

Example ```GET /partners/1```

### 3. Search partner by location:
GET /partners?lat={lat}&lng={lng}

Example ```GET /partners?lat=-23.89415&lng=-46.858412```
    
## Run Tests
Run command ```mvn test```

## Run
To run application was created a docker compose structure. When run command below will start redis, mongodb and application. After mongodb will be populated and ensure necessary indexes.

Run command ```docker-compose up```