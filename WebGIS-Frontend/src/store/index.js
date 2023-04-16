import { createStore, applyMiddleware } from "redux";
import thunkMiddleware from 'redux-thunk'

// 导入reducer
import reducer from "./reducer";

const storeEnhancer = applyMiddleware(thunkMiddleware)

const store = createStore(reducer, storeEnhancer)

export default store