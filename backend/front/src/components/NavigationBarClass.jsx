import React from 'react';
import { Navbar, Nav } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faHome, faUser} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from 'react-router-dom';
import Utils from "../utils/Utils";
import axios from "axios";
import {Link} from "react-router-dom";
import BackendService from "../services/BackendService";
import {connect} from 'react-redux'
import {userActions} from "../utils/Rdx";

class NavigationBarClass extends React.Component {
	constructor(props) {
		super(props);
		this.goHome = this.goHome.bind(this);
		this.logout = this.logout.bind(this);
	}
	goHome() {
		this.props.navigate('home');
	}
	logout() {
		BackendService.logout()
			.then(() => {
				Utils.removeUser();
				this.props.dispatch(userActions.logout())
				this.props.navigate('Login');
		});
	}
	render() {
		return (
			<Navbar bg="light" expand="lg">
				<Navbar.Brand><FontAwesomeIcon icon={faHome} />{' '}My RPO</Navbar.Brand>
				<Navbar.Brand>RPO_2022_IU3</Navbar.Brand>
				<Navbar.Toggle aria-controls="basic-navbar-nav" />
				<Navbar.Collapse id="basic-navbar-nav">
					<Nav className="ms-auto">
						{/*<Nav.Link href="/home">Home</Nav.Link>*/}
						<Nav.Link onClick={this.goHome}>Home</Nav.Link>                     
                      				<Nav.Link as={Link} to="/another_home">Information</Nav.Link>
						
					</Nav>
			</Navbar.Collapse>
			<Navbar.Text>{this.props.user && this.props.user.login}</Navbar.Text>
			{ this.props.user && <Nav.Link onClick={this.logout}><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Выход</Nav.Link>
			}
			{ !this.props.user && <Nav.Link as={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Вход</Nav.Link>
			}
			</Navbar>
		);
	}
}
const NavigationBar = props => {
	const navigate = useNavigate()
	return <NavigationBarClass navigate={navigate} {...props} />
}
const mapStateToProps = state => {
	const { user } = state.authentication;
	return { user };
}
export default connect(mapStateToProps)(NavigationBar);
