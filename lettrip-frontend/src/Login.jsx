import React, { useState } from 'react';

function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // Perform authentication here
    console.log(`Username: ${username}, Password: ${password}`);
  };

  return (
    <form onSubmit={handleSubmit}>
      <img src="C:\Users\chilo\lettrip\src\logo.png" alt="Logo" />
      <label>
       <p>ID</p>
        <input type="text" value={username} onChange={handleUsernameChange} />
      </label>
      <br />
      <label>
       <p>PW</p>
        <input type="password" value={password} onChange={handlePasswordChange} />
      </label>
      <br />
      <button type="submit">LOGIN</button>
     
    </form>
  );
}

export default LoginForm;