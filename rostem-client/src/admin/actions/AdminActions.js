import * as rostemConstants from "../../constants/constants";
import axios from "axios";

export const getUsers = async () => {
  let response = await axios.get(rostemConstants.BASE_URL + "/admin/users");
  return response;
};
