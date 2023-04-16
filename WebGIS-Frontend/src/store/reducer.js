import { combineReducers } from "redux";
import { obtainHomeOnScreenData } from "../pages/homeMap/store";

const reducer = combineReducers({
  HomeOnScreenData: obtainHomeOnScreenData
})

export default reducer