import React from "react";
import { BrowserRouter, Routes } from "react-router-dom";
import {createBrowserHistory} from "history";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Router, Route } from "react-router";
import '/home/rustam/AndroidStudioProjects/backend/front/src/App.css';
import Home from "/home/rustam/AndroidStudioProjects/backend/front/src/components/Home";
import Login from "/home/rustam/AndroidStudioProjects/backend/front/src/components/Login";
import Another_home from "/home/rustam/AndroidStudioProjects/backend/front/src/components/Another_home";
import NavigationBar from "/home/rustam/AndroidStudioProjects/backend/front/src/components/NavigationBarClass";
import Utils from "./utils/Utils";
import {connect} from "react-redux";
import Navigate from "/home/rustam/AndroidStudioProjects/backend/front/src/components/NavigationBarClass";
import SideBar from "./components/SideBar";
import {useState} from "react";
import CountryListComponent from "./components/CountryListComponent";
import CountryComponent from "./components/CountryComponent";

const ProtectedRoute = ({children}) => {
	let user = Utils.getUser();
	return user ? children : <Navigate to={'/login'} />
};

const App = props => {
	const [exp,setExpanded] = useState(true);
	return (
		<div className="App">
			<BrowserRouter>
				<NavigationBar toggleSideBar={() =>
					setExpanded(!exp)}/>
					<div className="wrapper">
					<SideBar expanded={exp} />
					<div className="container-fluid">
						{ props.error_message && <div className="alert alert-danger m-1">{props.error_message}</div>}
							<Routes>
								<Route path="login" element={<Login />}/>
								<Route path="home" element={<ProtectedRoute><Home/></ProtectedRoute>}/>
								<Route path="Another_home" element={<ProtectedRoute>  <Another_home/> </ProtectedRoute>}/>
								<Route path="countries" element={<ProtectedRoute> <CountryListComponent/> </ProtectedRoute>}/>
								<Route path="countries/:id" element={<ProtectedRoute><CountryComponent /></ProtectedRoute>}/>
							</Routes>
					</div>
					</div>
			</BrowserRouter>
		</div>
	);
}
const mapStateToProps = function (state) {
	const { msg } = state.alert;
	return { error_message: msg };
}
export default connect(mapStateToProps)(App);
