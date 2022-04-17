
import React from "react";
import { BrowserRouter, Routes } from "react-router-dom";
import {createBrowserHistory} from "history";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Router, Route } from "react-router";
import '/home/rustam/AndroidStudioProjects/backend/front/src/App.css';
import Home from "/home/rustam/AndroidStudioProjects/backend/front/src/components/Home";
import Another_home from "/home/rustam/AndroidStudioProjects/backend/front/src/components/Another_home";
import NavigationBar from "/home/rustam/AndroidStudioProjects/backend/front/src/components/NavigationBarClass";

function App() {
	return (
		<div className="App">
			<BrowserRouter>
				<NavigationBar />
				<div className="container-fluid">
					<Routes>
						<Route path="home" element={<Home />}/>
						{         }
						<Route path="Another_Home" element={<Another_home />} />
					</Routes>
				</div>
			</BrowserRouter>
		</div>
	);
}
export default App;
