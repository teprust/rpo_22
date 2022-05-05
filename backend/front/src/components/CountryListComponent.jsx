import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash, faEdit, faPlus } from '@fortawesome/free-solid-svg-icons'
import Alert from './Alert'
import BackendService from "../services/BackendService";
import { useNavigate } from 'react-router-dom';

const CountryListComponent = props => {
    //message – это текст в окне Alert, который будет меняться в зависимости от того одну страну мы удаляем или несколько.
    const [message, setMessage] = useState();
    const [countries, setCountries] = useState([]);

    //selected_countries – это список отмеченных для удаления стран, который создается с помощью списка checkedItems и передается в запросе на удаление стран
    //REST сервису. Модального окно Alert для подтверждения удаления стран, отображается на экране, если флаг show_alert равен true.
    const [selectedCountries, setSelectedCountries] = useState([]);

    //Модального окно Alert для подтверждения удаления стран, отображается на экране, если флаг show_alert равен true.
    const [show_alert, setShowAlert] = useState(false);

    //В каждой строке таблицы стран у нас будет чек бокс, с помощью которого можно отметить страну, с тем чтобы потом по кнопке удалить сразу все отмеченные
    // страны. checkedItems это список двоичных флагов, каждый из которых хранит состояние чек бокса одной из строк таблицы стран: true – отмечен, false – не
    // отмечен.
    const [checkedItems, setCheckedItems] = useState([]);

    //Логический флаг hidden для запрета отображения
    // списка стран в случае, если запрос к сервису завершился с ошибкой. В этом случае будем рисовать пустую страницу.
    const [hidden, setHidden] = useState(false);

    const navigate = useNavigate();

    //Функция setChecked отмечает или снимает отметку всех чек боксов. В заголовок таблицы мы вставим чек бокс, который позволит нам устанавливать или
    // сбрасывать все чек боксы в строках таблицы с помощью этой функции. Мы не можем просто поменять значение в поле состояния checkedItems. Поэтому делаем
    // копию этого поля и заменяем его на эту копию с помощью функции setCheckedItemsState.

    const setChecked = v => {
        setCheckedItems(Array(countries.length).fill(v));
    }

    //Функция handleCheckChange использует принцип контролируемого ввода, о котором уже упоминалось раньше. При изменении состояния чек бокса вызывается
    // эта функция, которая записывает состояние чек бокса в поле состояния checkedItems. Здесь также делаем копию, вносим в нее изменения и сохраняем обратно в
    // состояние с помощью setCheckedItems.

    const handleCheckChange = e => {
        const idx = e.target.name;
        const isChecked = e.target.checked;
        let checkedCopy = [...checkedItems];
        checkedCopy[idx] = isChecked;
        setCheckedItems(checkedCopy);
    }

    //handleGroupCheckChange – это обработчик чек бокса в заголовке таблицы. Он устанавливает или сбрасывает все чек боксы в строках таблицы.

    const handleGroupCheckChange = e => {
        const isChecked = e.target.checked;
        setChecked(isChecked);
    }

    //Функция deleteCountriesClicked вызывается по кнопке Удалить. Она с помощью функции map, которая аналогична циклу, пробегает по всем странам и,
    // одновременно, по всем элементам checkedItems для того, чтобы определить какие страны отмечены для удаления. Эти страны помещаются в массив x. Если
    // массив оказывается пустым, то на этом все заканчивается. Вообще говоря можно вывести Alert с соответствующим сообщением. Если в списке ровно одна
    // страна, то с помощью модального окна Alert спрашиваем подтверждение удаления этой страны. Если стран несколько, делаем тоже самое, но изменяем текст
    // сообщения.

    const deleteCountriesClicked = () => {
        let x = [];
        countries.map ((t, idx) => {
            if (checkedItems[idx]) {
                x.push(t)
            }
            return 0
        });
        if (x.length > 0) {
            var msg;
            if (x.length > 1) {
                msg = "Пожалуйста подтвердите удаление " + x.length + " стран";
            }
            else
            {
                msg = "Пожалуйста подтвердите удаление страны " + x[0].name;
            }
            setShowAlert(true);
            setSelectedCountries(x);
            setMessage(msg);
        }
    }

    //Функция refreshCountries получает список стран с помощью запроса к REST сервису и помещает в поле состояния countries. В случае ошибки, устанавливает флаг
    // hidden, который используется в методе render для того, чтобы убрать с экрана изображение таблицы.

    const refreshCountries = () => {
        BackendService.retrieveAllCountries()
            .then(
                resp => {
                    setCountries(resp.data);
                    setHidden(false);
                })
            .catch(()=> { setHidden(true )})
            .finally(()=> setChecked(false))
    }


    //Хук useEffect вызывается каждый раз при перерисовке страницы. Он используется для загрузки списка стран. Второй параметр useEffect ставит его вызов в
    // зависимость от изменения свойств useState. В нашем случае список зависимостей пуст, поэтому useEffet в данном случае аналогичен методу ComponentDidMount
    // в компоненте-классе, который обычно используется для загрузки всех данных необходимых для прорисовки страницы.

    useEffect(() => {
        refreshCountries();
    }, [])



    //Функция updateCountryClicked обрабатывает нажатие кнопки для перехода к странице редактирования страны. Такая кнопка есть в каждой строчке таблицы
    // стран.

    const updateCountryClicked = id => {
        navigate(`/countries/${id}`)
    }


    //Модальное окно Alert имеет две кнопки: Ok и Cancel. По кнопке Cancel вызывается функция closeAlert, которая закрывает окно. По кнопке Ok сначала
    // вызывается closeAlert, а потом onDelete, которая вызывает REST сервис для удаления списка стран и, в случае успешного завершения этой операции, обновляет
    // список оставшихся стран на экране.

    const onDelete = () => {
        BackendService.deleteCountries(selectedCountries)
            .then( () => refreshCountries())
            .catch(()=>{})
    }
    const closeAlert = () => {
        setShowAlert(false)
    }


    //Новая страна добавляется по кнопке Добавить над таблицей. Эта кнопка связана с обработчиком addCountryClicked. Обратите внимание на то, что при
    // добавлении страны мы указываем в URL индекс -1.

    const addCountryClicked = () => {
        navigate(`/countries/-1`)
    }

    //В начале метода render, если hidden равно false, возвращаем null ничего не рисуя. Иначе, выводим заголовок и две кнопки для добавления и удаления стран. В
    // заголовок таблицы, как обещано, вставлен чек бокс. Для вывода строк таблицы используется уже известный нам оператор map. После таблицы всавлено
    // модальное окно Alert, которое управляется атрибутом modal.

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2">
                <h3>Страны</h3>
                <div className="btn-toolbar">
                    <div className="btn-group ms-auto">
                        <button className="btn btn-outline-secondary"
                                onClick={addCountryClicked}>
                            <FontAwesomeIcon icon={faPlus} />{' '}Добавить
                        </button>
                    </div>
                    <div className="btn-group ms-2">
                        <button className="btn btn-outline-secondary"
                                onClick={deleteCountriesClicked}>
                            <FontAwesomeIcon icon={faTrash} />{' '}Удалить
                        </button>
                    </div>
                </div>
            </div>
            <div className="row my-2 me-0">
                <table className="table table-sm">
                    <thead className="thead-light">
                    <tr>
                        <th>Название</th>
                        <th>
                            <div className="btn-toolbar pb-1">
                                <div className="btn-group ms-auto">
                                    <input type="checkbox" onChange={handleGroupCheckChange} />
                                </div>
                            </div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        countries && countries.map((country, index) =>
                            <tr key={country.id}>
                                <td>{country.name}</td>
                                <td>
                                    <div className="btn-toolbar">
                                        <div className="btn-groupms-auto">
                                            <button className="btn btn-outline-secondary btn-sm btn-toolbar"
                                                    onClick={() =>
                                                        updateCountryClicked(country.id)}>
                                                <FontAwesomeIcon icon={faEdit} fixedWidth />
                                            </button>
                                        </div>
                                        <div className="btn-group ms-2 mt-1">
                                            <input type="checkbox" name={index}
                                                   checked={checkedItems.length> index ?
                                                       checkedItems[index] : false}
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
                   cancelButton={true} />
        </div>
    )
}
export default CountryListComponent;


