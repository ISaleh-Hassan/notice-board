
function createUser() {
    fetch('http://localhost:8080/api/create/useraccount', {
        method: 'Post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: 4, username: 'Test Testsson', password: 'bro'})
    })  
    .then(response => {return response.json()})
    .catch(error => console.log('The User was not added'));
    }

function getAllUsers() {
  fetch('http://localhost:8080/api/fetch/useraccount/all')  
  .then(response => response.json())
  .then(data => console.log(data));
  }

function getUserById(id) {
return fetch('http://localhost:8080/api/fetch/useraccount/' + id)  
.then(response => response.json())
.then(data => data);
}
//TODO:This method don't work yet.
function updateUser(id) {
    fetch('http://localhost:8080/api/update/useraccount/' + id, {
        method: 'Patch',
        
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: "This is not my upinions"})
    })  
    .then(response => {return response.json()})
    .catch(error => console.log('Error to update User'));
    }

    function deleteUser(id) {
        fetch('http://localhost:8080/api/delete/useraccount/' + id, {
            method: 'Delete',
        })  
      
        .catch(error => console.log('The User was not deleted'));
        }
function getAllUsers() {
  fetch('http://localhost:8080/api/fetch/useraccount/all')  
  .then(response => response.json())
  .then(data => console.log(data));
  }

function getUserById(id) {
    return fetch('http://localhost:8080/api/fetch/useraccount/' + id)  
    .then(response => response.json())
    .then(data => data);
    }

//TODO:This method don't work yet.
function updateUser(id) {
    fetch('http://localhost:8080/api/update/useraccount/' + id, {
        method: 'Patch',
        
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: "This is not my upinions"})
    })  
    .then(response => {return response.json()})
    .catch(error => console.log('Error to update User'));
    }

    function deleteUser(id) {
        fetch('http://localhost:8080/api/delete/useraccount/' + id, {
            method: 'Delete',
        })  
      
        .catch(error => console.log('The User was not deleted'));
        }

var UsersDiv = document.getElementById('postauthor');
var posts = document.getElementById('posttext');
var Users = document.getElementById('posttext');

function addPost () { 
    // create a new div element 
    const postText = document.createElement("p"); 
    // and give it some content 
    const newContent = document.createTextNode("Hi there and greetings!"); 
    
    // add the text node to the newly created div
    newDiv.appendChild(newContent);  
  
    // add the newly created element and its content into the DOM 
    const currentDiv = document.getElementById("div1"); 
    document.body.insertBefore(newDiv, currentDiv); 
  }

