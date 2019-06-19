import axios from "axios";

export const BASE_URL = "http://localhost:8080";

export const axiosRequest = axios.create({
  withCredentials: true
});

export async function userLoggedIn() {
  const req = axios.create({
    withCredentials: true
  });
  req
    .post(BASE_URL + "/login/getdetails")
    .then(response => {
      if (response.data.object.role !== "ROLE_USER") {
        this.props.history.push("/forbidden");
      } else {
        return response.data.object.user.email;
      }
    })
    .catch(error => {
      return false;
    });
}
