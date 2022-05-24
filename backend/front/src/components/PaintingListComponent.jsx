import React, {useState, useEffect} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faTrash, faEdit, faPlus} from '@fortawesome/free-solid-svg-icons'
import Alert from './Alert'
import BackendService from "../services/BackendService";
import {useNavigate} from 'react-router-dom';
import PaginationComponent from "./PaginationComponent";

const PaintingListComponent = props => {
    const [message, setMessage] = useState();
    const [paintings, setPaintings] = useState([]);
    const [selectedArtists, setSelectedArtists] = useState([]);
    const [show_alert, setShowAlert] = useState(false);
    const [checkedItems, setCheckedItems] = useState([]);
    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();

    // Добавляем из 12 лабы
    const [page, setPage] = useState(0);
    const [totalCount, setTotalCount] = useState(0);
    const limit = 2;

    const setChecked = v => {
        setCheckedItems(Array(paintings.length).fill(v));
    }

    // Функция загрузки страницы
    const onPageChanged = cp => {
        refreshPaintings(cp - 1);
    }

    const handleCheckChange = e => {
        const idx = e.target.name;
        const isChecked = e.target.checked;

        let checkedCopy = [...checkedItems];
        checkedCopy[idx] = isChecked;
        setCheckedItems(checkedCopy);
    }

    const handleGroupCheckChange = e => {
        const isChecked = e.target.checked;
        setChecked(isChecked);
    }

    const deletePaintingClicked = () => {
        let x = [];
        paintings.map((t, idx) => {
            if (checkedItems[idx]) {
                x.push(t)
            }
            return 0
        });

        if (x.length > 0) {
            var msg;
            if (x.length > 1) {
                msg = "Пожалуйста подтвердите удаление " + x.length + " картин";
            } else {
                msg = "Пожалуйста подтвердите удаление картины " + x[0].name;
            }
            setShowAlert(true);
            setSelectedArtists(x);
            setMessage(msg);
        }
    }

    const refreshPaintings = cp => {
        BackendService.retrieveAllPaintings(cp, limit).then(
            resp => {
                setPaintings(resp.data.content);
                setHidden(false);
                setTotalCount(resp.data.totalElements);
                setPage(cp);
            }
        ).catch(() => {
            setHidden(true);
            setTotalCount(0);
        }).finally(() => setChecked(false))

        //console.log(paintings)
    }

    useEffect(() => {
        refreshPaintings();
    }, [])

    const updatePaintingClicked = id => {
        console.log(id)
        navigate(`/paintings/${id}`)
    }

    const onDelete = () => {
        BackendService.deletePaintings(selectedArtists)
            .then(() => {
                refreshPaintings()
            })
            .catch(() => {
            })
    }

    const closeAlert = () => {
        setShowAlert(false)
    }

    const addPainting = () => {
        navigate(`/paintings/-1`)
    }

    if (hidden) {
        return null;
    }

    return (
        <div className="m-4">
            <div className="row my-2">
                <h3>Картины</h3>
                <div className="btn-toolbar">
                    <div className="btn-group ms-auto">
                        <button className="btn btn-outline-secondary"
                                onClick={addPainting}>
                            <FontAwesomeIcon icon={faPlus}/>{' '}Добавить
                        </button>
                    </div>
                    <div className="btn-group ms-2">
                        <button className="btn btn-outline-secondary"
                                onClick={deletePaintingClicked}>
                            <FontAwesomeIcon icon={faTrash}/>{' '}Удалить
                        </button>
                    </div>
                </div>
            </div>

            <div className="row my-2 me-0">
                <PaginationComponent
                    totalRecords={totalCount}
                    pageLimit={limit}
                    pageNeighbours={1}
                    onPageChanged={onPageChanged}/>
                <table className="table table-sm">
                    <thead className="thead-light">
                    <tr>
                        <th>Название</th>
                        <th>Музей</th>
                        <th>Художник</th>
                        <th>Год</th>
                        <th>
                            <div className="btn-toolbar pb-1">
                                <div className="btn-group ms-auto">
                                    <input type="checkbox" onChange={handleGroupCheckChange}/>
                                </div>
                            </div>
                        </th>
                    </tr>
                    </thead>

                    <tbody> {
                        paintings && paintings.map((painting, index) =>
                            <tr key={painting.id}>
                                <td>{painting.name}</td>
                                <td>{painting.museumid.name}</td>
                                <td>{painting.artistid.name}</td>
                                <td>{painting.year}</td>
                                <td>
                                    <div className="btn-toolbar">
                                        <div className="btn-group ms-auto">
                                            <button className="btn btn-outline-secondary btn-sm btn-toolbar"
                                                    onClick={() =>
                                                        updatePaintingClicked(painting.id)}>
                                                <FontAwesomeIcon icon={faEdit} fixedWidth/>
                                            </button>
                                        </div>

                                        <div className="btn-group ms-2 mt-1">
                                            <input type="checkbox" name={index}
                                                   checked={checkedItems.length > index ? checkedItems[index] : false}
                                                   onChange={handleCheckChange}/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        )
                    }
                    </tbody>
                </table>
            </div>

            <Alert title="Удаление"
                   message={message}
                   ok={onDelete}
                   close={closeAlert}
                   modal={show_alert}
                   cancelButton={true}/>
        </div>
    )
}

export default PaintingListComponent;