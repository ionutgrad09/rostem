import * as rostemConstants from "../../constants/constants";
import axios from "axios";

export function register(email, username, password) {
  axios
    .post(rostemConstants.BASE_URL + "/register", { email, password, username })
    .then(res => {
      console.log(res);
    });
}

export async function login(email, password) {
  const headers = {
    "Content-Type": "application/x-www-form-urlencoded"
  };
  const body = "username=" + email + "&password=" + password;

  return axios.post(rostemConstants.BASE_URL + "/login", body, {
    headers: headers
  });
}
