# Kick Social Media Platform

Kick is a platform being designed to help music creators be able to share their work with others in a social-media like experience. The goal is to be able to bring together musicians to foster a collaborative environment as well as help musicians advertise their work and profit from it. This project was a result of personal interest in getting to learn full-stack development as well as a personal passion for making music.

## Front End

Front end built using React. Global state managed through Redux. Deployed using Firebase and AWS Route 53.  
Deployed at: https://kick-share.com  
Read more at: https://github.com/barbinimichael/Kick/blob/master/FrontEnd/README.md

### Notable Features

- Authenticate with username and password
- Personalized user feed based on user-connections (loads more content as user scrolls to limit querying)
- Create and interact with other posts
- Message other users, create group chats
- Follow other users
- Public / private profiles
- Explore page for viewing random posts
- Search for other users and posts
- Recieve notifications about interactions (following requests and others liking posts)

## Back End & Database

Back end REST api built using Spring. PostgreSQL database storing all data, with data access using JPA. Deployed using AWS EC2.  
Deployed at: https://api.kick-share.com  
Read more at: https://github.com/barbinimichael/Kick/blob/master/BackEnd/README.md

### Notable Features

- Authenticate with to recieve JWT token, can only access data that user is associated with
- Stores users, user-connections, comments, likes, notifications, posts
