# User create and login Service
CRUD operations are implemented for the users and the data is stored in mysql, redis and elastic serach, cache for get apis are created by redis cache. Follows the three tier architecture of building microservices.

# Endpoints

## Get
/users/check : Gives the string response, just to check microservice is up and running.

/users/all : Gives the list of UserResponseModel, provides all the users stored in database.

/users/{id} : Gives the user with specified id, gives response in UserResponseModel.

/users/elastic/{userId}: Give the user with the specifies userId from the elastic search, gives response in UsersElasticModel.

/users/redis/{userId}: Give the user with the specifies userId from the redis data, gives response in UserRedisModel, also if hit again gets the user from redis cache.

## Post

/users/create: create the user store it in mysql database, redis, and elastic search, returns the userId for public use, takes UserRequestModel.

/login: Logs in the user and authenticate and provides jwt token, takes LoginRequestModel.

## Put

/users/{id}: Updates the user with specific id in mysql, redis data and cache, elastic search, return UserResponseModel.

## Delete

/users/{id}: Deletes the specific user from Mysql, Redis Data and chache, elastic search, return String.

# Models

## LoginRequestModel:
{
    "email":String,
    "Password": String
}
## UserRequestModel:
{
    firstName:String,
    lastName:String,
    email:String,
    password:String
}
## UserResponseModel:
{
    firstName:String,
    lastName:String,
    email:String,
    userId:String
}
## UserElasticModel:
{
    firstName:String,
    lastName:String,
    email:String,
    userId:String,
    encryptedPassword:String
}
## UserRedisModel
{
    firstName:String,
    lastName:String,
    email:String,
    userId:String,
    encryptedPassword:String
}


