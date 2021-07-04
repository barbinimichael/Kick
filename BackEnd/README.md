# Kick Backend

The Kick API services the Kick Web Application.

## Deployment

~~This project has been deployed at https://api.kick-share.com using Route 53 and EC2 from Amazon Web Service. Configured with HTTPS encryption. It has a mySQL database storing all data.~~  
Deployed at: https://quiet-inlet-83310.herokuapp.com/  
_(This will cause boot-up times but I need my AWS free-tier for my next project)_

## Technologies

The Kick API is based on REST. It accepts and returns with the JSON format, responds with standard HTTP codes,
and is hyper-media driven.  
The Kick API is built on-top of the Spring Framework. It utilizes its out-of-the-box solutions for
the application, security, Java Persistence API (JPA) repositories, controllers, and testing.  
The Kick API authenticates using Java Web Tokens (JWT). After the server verifies the user, it sends a JWT which
the user sends back in each request's header.  
Maven manages the project's build and compilation.

## Test Configuration

When configured for local build, the server can be found at [localhost:8080](). This can be changed in
Kick\BackEnd\kick\src\main\resources\application.properties

---

## Sign-up

In order to sign-up and create a new user, send a POST request to [/api/applicationUsers/sign-up]() with
the minimum information in the body:

- username
- password

[Refer to Users to see remaining attributes](#users)  
Example Request Body, JSON:

```JSON
{
    "username" : "new user",
    "password" : "password"
}
```

---

## Login

In order to login, send a POST request to [/api/login]() with the body containing:

- username
- password

Example Request Body, JSON:

```JSON
{
    "username" : "username",
    "password" : "password"
}
```

---

## Users

#### Attributes

id:

> The user's id
> Value: long

username

> The user's username
> Value: unicode characters

password

> The user's password
> Value: unicode characters

firstName

> The user's first name  
> Value: unicode characters

lastName

> The user's last name  
> Value: unicode characters

email

> The user's email  
> Value: unicode characters

birthday

> The user's birthday  
> Value: unicode characters  
> ISO-8601 representation  
> Example: 1970-01-01T00:00:00Z

city

> The user's city they currently live in  
> Value: unicode characters

country

> The user;s country they currently live in  
> Value: unicode characters

gender

> The user's gender  
> Value: enum (MALE, FEMALE, OTHER)

biography

> The user's biography  
> Value: unicode characters

profilePictureURL

> The link to the user's profile picture  
> Value: unicode characters

privateProfile

> Whether the user's profile / posts are private to non-followers  
> Value: boolean (true, false)

posts

> The user's posts  
> Value: List of Post Entities

influencers

> The list of accounts this user follows
> Value: List of Following Entities

followers

> The list of accounts that follow this user  
> Value: List of Following Entities

likes

> The user's likes they have left on posts  
> Value: List of LikePost Entities

comments

> The user's comments they have left on posts  
> Value: List of CommentPost Entities

#### Retrieve a User

If the user exists and is equal to this user:

> Retrieve all user information

If the user exists and public:

> Retrieve all user public information

If the user exists and is private, this user does not follow:

> Retrieve all user private information

If the user exists and is private, this user does follow:

> Retrieve all user public information

If the user does not exist:

> 404 error

**Retrieving this user profile**  
Get request to [/api/applicationUsers/self]()

**Searching a profile**  
Get request to [/api/applicationUsers/search?search={params}]()

**Retrieve a profile using user id**  
Get request to [/api/applicationUsers/id/{id}]()

**Retrieve a profile using user username**  
Get request to [/api/applicationUsers/username/{username}]()

#### Update a User

**Update this user username**  
Put request to [/api/applicationUsers/username]()

**Update this user password**  
Put request to [/api/applicationUsers/password]()

**Update this user email**  
Put request to [/api/applicationUsers/email]()

**Update this user city**  
Put request to [/api/applicationUsers/city]()

**Update this user country**  
Put request to [/api/applicationUsers/country]()

**Update this user biography**  
Put request to [/api/applicationUsers/biography]()

**Update this user profile picture URL**  
Put request to [/api/applicationUsers/profilePictureURL]()

**Update this user private profile status**  
Put request to [/api/applicationUsers/privateProfile]()

#### Deleting a user

Delete request to [/api/applicationUsers]()

---

## Posts

#### Attributes

id:

> The post's id
> Value: long

user:

> The user that created the post
> Value: User

likes:

> The post's likes  
> Value: List of LikePost Entities

comments:

> The post's comments  
> Value: List of CommentPost Entities

caption:

> The post's caption
> Value: unicode characters

imageURL:

> The post's image URL
> Value: unicode characters

city:

> The city the post was made from
> Value: unicode characters

country:

> The country the post was made from
> Value: unicode characters

postDate:

> The post date of the post
> Value: unicode characters  
> ISO-8601 representation  
> Example: 1970-01-01T00:00:00Z

#### Retrieve a Post

If the post exists and this user created it:

> Retrieve the post

If the post exists, and the user is public:

> Retrieve the post

If the post exists, and the user is private, this user does follow

> Retrieve the post

If the post exists, and the user is private, this user does not follow

> 403 error

If the post does not exist:

> 404 error

**Searching a post**  
Get request to [/api/posts/search?search={params}]()

**Retrieve a profile using id**  
Get request to [/api/posts?id={id}]()

#### Create a Post

Post mapping to [/api/posts]()  
[Refer to Post for attributes](#posts)

#### Update a Post Caption

Put request to [/api/posts]()  
The body must contain the new caption

#### Deleting a Post

Delete request to [/api/posts?id={id}]()  
This user must own this Post

---

## Followings

#### Attributes

id:

> The post's id
> Value: long

influencer:

> The influencer in the Following relationship
> Value: User

follower:

> The follower in the Following relationship
> Value: User

accepted:

> Whether the influencer accepted the follower's request  
> Value: boolean (true, false)
> Will be set to true automatically if the influencer has a public profile status

#### Retrieve a Following

If the following exists and this user is the influencer or follower:

> Retrieve the following

If the post exists, and the user is not the influencer or follower:

> 403 error

If the following does not exist:

> 404 error

**Retrieve a following using id**  
Get request to [/api/followings?id={id}]()

#### Create a Following

Post mapping to [/api/followings]()  
[Refer to Following for attributes](#followings)

#### Respond to a Following Request

Post request to [/api/followings/{id}/{condition}]()  
The body must contain a boolean representing the response

#### Deleting a Following

Delete request to [/api/followings/{id}]()  
This user must be part of the Following relationship

---

## Likes

#### Attributes

id:

> The like's id
> Value: long

user:

> The user that made the like
> Value: User

post:

> The post the like was left on
> Value: Post

#### Retrieve a Like

If the like exists and this user left the like:

> Retrieve the like

If the like exists, and the user did not leave the like:

> 403 error

If the like does not exist:

> 404 error

**Retrieve a following using id**  
Get request to [/api/likes?id={id}]()

#### Create a Like

Post mapping to [/api/likes]()  
[Refer to Likes for attributes](#Likes)

#### Deleting a Like

Delete request to [/api/followings/{id}]()  
This user must own the like

---

## Comments

#### Attributes

id:

> The like's id
> Value: long

user:

> The user that made the like
> Value: User

post:

> The post the like was left on
> Value: Post

comment:

> The comment's text
> Value: unicode characters

#### Retrieve a Comment

If the comment exists and this user left the comment:

> Retrieve the following

If the comment exists and this user did not leave the comment:

> 403 error

If the comment does not exist:

> 404 error

**Retrieve a comment using id**  
Get request to [/api/comments?id={id}]()

#### Create a Comment

Post mapping to [/api/comments]()  
[Refer to Comment for attributes](#comments)

#### Edit a Comment

Put request to [/api/comments/{id}]()  
The body must contain the new comment text

#### Deleting a Comment

Delete request to [/api/comments/{id}]()  
This user must own the comment
