# Java + Hibernate + Postgres App
Builds a comments table with likes + nested comment capability

## Setup
Ensure you have PostgreSQL installed locally

## Capabilities
### Get `/api/comments`
- Fetches the top level parent comments in a pageable manner

### Post `/api/comments`
- Can post comments to the DB
- Can nest comments if parentCommentId provided
```
    content: "comment to be added"
    likes: 2 
    parentCommentId: (nullable/optional) 1
```

### Put `/api/comments/{commentId}`
- Updates the comment with new values
```
    content: "comment to be added"
    likes: 2 
```

### Delete `/api/comments/{commentId}`
- Deletes the specified comment

