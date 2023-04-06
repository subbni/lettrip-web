import React from "react";
import Login from "./Login";

<form onSubmit={handleSubmit}>
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
