

function createComment() {
    fetch('http://localhost:8080/api/create/comment', {
        method: 'Post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: 4, message: "Everyone disagree with you. ", postId: 2, userAccountId: 3})
    })  
    .then(response => {return response.json()})
    .catch(error => console.log('The comment was not added'));
    }

function getAllComments() {
    fetch('http://localhost:8080/api/fetch/comment/all').then(response => 
        response.json().then(data => ({
            data: data,
            status: response.status
        })
    ).then(res => {
        if(res.status=200){
           console.log(res.data)
        }     
    }));
}
function getCommentById(id) {
return fetch('http://localhost:8080/api/fetch/comment/' + id)  
.then(response => response.json())
.then(data => data);
}
//TODO:This method don't work yet.
function updateComment(id) {
    fetch('http://localhost:8080/api/update/comment/' + id, {
        method: 'Patch',
        
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: "This is not my upinions"})
    })  
    .then(response => {return response.json()})
    .catch(error => console.log('Error to update comment'));
    }

    function deleteComment(id) {
        fetch('http://localhost:8080/api/delete/comment/' + id, {
            method: 'Delete',
        })  
      
        .catch(error => console.log('The comment was not deleted'));
        }



  