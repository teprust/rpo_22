import Utils from "./Utils";

import {applyMiddleware, combineReducers, createStore} from "redux";
import {createLogger} from "redux-logger"
import axios from "axios";



/* ACTIONS */
const userConstants = {
	LOGIN: 'USER_LOGIN',
	LOGOUT: 'USER_LOGOUT'
};
/* ACTION GENERATORS */
export const userActions = {
	login,
	logout
};
function login(user) {
	Utils.saveUser(user)
	return { type: userConstants.LOGIN, user }
}
function logout() {
	Utils.removeUser()
	return { type: userConstants.LOGOUT }
}

/* REDUСERS */
let user = Utils.getUser()
const initialState = user ? { user } : {}
function authentication(state = initialState, action) {
	console.log("authentication")
	switch (action.type) {
		case userConstants.LOGIN:
			return { user: action.user };
		case userConstants.LOGOUT:
			return { };
		default:
			return state
	}
}
//----------------------------------------------------


//-----------------------------------------------

/* ACTIONS */
const alertConstants = {
	ERROR: 'ERROR',
	CLEAR: 'CLEAR',
};
/*Первый фрагмент кода файла Rdx.jsx включает объявление действий и генераторов действий.*/
/* ACTION GENERATORS */

export const alertActions = {
	error,
	clear
};
function error(msg) {
	return { type: alertConstants.ERROR, msg }
}
function clear() {
	return { type: alertConstants.CLEAR }
}




/* REDUСERS */
/*В проекте может быть несколько логически не связанных между собой
процессов изменяющих состояние хранилища redux. Например, вход/выход и обработка ошибок REST интерфейса. Эти процессы затрагивают разные области
хранилища и их обработку логично разделить в коде приложения. Пока у нас есть один reducer с именем authentication.*/
function alert(state = {}, action) {
	console.log("alert")
	switch (action.type) {
		case alertConstants.ERROR:
			return { msg: action.msg };
		case alertConstants.CLEAR:
			return { };
		default:
			return state
	}
}


/* STORE */
/*Функция combineReducers нужна для того чтобы составить список reduser’ов.*/
const rootReducer = combineReducers({
	authentication, alert
});
const loggerMiddleware = createLogger();
export const store = createStore(
	rootReducer,
	applyMiddleware( loggerMiddleware )
);

