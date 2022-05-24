import React, {useEffect, useState} from 'react';
import BackendService from '../services/BackendService';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
//import {faChevronLeft, faSave} from '@fortawesome/fontawesome-free-solid';
import {alertActions} from "../utils/Rdx";
import {connect} from "react-redux";
import {Form} from "react-bootstrap";
import {useNavigate, useParams} from "react-router-dom";
import {faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";

const PaintingComponent = props => {
    const params = useParams();

    const [id, setId] = useState(params.id);
    const [name, setName] = useState("");
    const [year, setYear] = useState(0);

    // Приходится использовать пока что дефолтное значение внешнего ключа, иначе будет ошибка
    const [museumid, setMuseumid] = useState(2);
    const [artistid, setArtistid] = useState(2);

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (parseInt(id) !== -1) {
            BackendService.retrievePainting(id)
                .then((resp) => {
                    setName(resp.data.name)
                    setYear(resp.data.year)

                    setArtistid(resp.data.artistid);
                    setMuseumid(resp.data.museumid);
                })
                .catch(() => setHidden(true))
        }
    }, []); // [] нужны для вызова useEffect только один раз при инициализации компонента
    // это нужно для того, чтобы в состояние name каждый раз не записывалось значение из БД

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;

        if (!name) err = "Название художника должно быть указано";
        if (!year) err = "Возраст художника должен быть указан";

        if (err) props.dispatch(alertActions.error(err));
        let painting = {id, name, museumid, artistid, year};

        //console.log(painting)

        if (parseInt(painting.id) === -1) {
            BackendService.createPainting(painting)
                .then(() => navigate(`/paintings`))
                .catch(() => {
                })
        } else {
            BackendService.updatePainting(painting)
                .then(() => navigate(`/paintings`))
                .catch(() => {
                })
        }
    }

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className=" row my-2 mr-0">
                <h3>Картина</h3>
                <button className="btn btn-outline-secondary ml-auto"
                        onClick={() => navigate(`/paintings`)}
                ><FontAwesomeIcon icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Имя</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите имя картины"
                        onChange={(e) => {setName(e.target.value)}}
                        value={name}
                        name="name"
                        autoComplete="off"
                    />

                    <Form.Label>Возраст</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите год"
                        onChange={(e) => {setYear(e.target.value)}}
                        value={year}
                        name="age"
                        autoComplete="off"
                    />
                </Form.Group>
                <button className="btn btn-outline-secondary" type="submit">
                    <FontAwesomeIcon icon={faSave}/>{' '}
                    Сохранить
                </button>
            </Form>
        </div>
    )
}

export default connect()(PaintingComponent);